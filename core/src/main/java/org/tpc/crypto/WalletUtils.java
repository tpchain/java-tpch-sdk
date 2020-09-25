/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.tpc.crypto;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import static org.web3j.crypto.Hash.sha256;
import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;
import static org.web3j.crypto.Keys.PRIVATE_KEY_LENGTH_IN_HEX;

/** Utility functions for working with Wallet files. */
public class WalletUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String generateFullNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
                    InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(password, destinationDirectory, true);
    }

    public static String generateLightNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
                    InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(password, destinationDirectory, false);
    }

    public static String generateNewWalletFile(String password, File destinationDirectory)
            throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
                    NoSuchProviderException, IOException {
        return generateFullNewWalletFile(password, destinationDirectory);
    }

    public static String generateNewWalletFile(
            String password, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException, InvalidAlgorithmParameterException,
                    NoSuchAlgorithmException, NoSuchProviderException {

        org.web3j.crypto.ECKeyPair ecKeyPair = org.web3j.crypto.Keys.createEcKeyPair();
        return generateWalletFile(password, ecKeyPair, destinationDirectory, useFullScrypt);
    }

    public static String generateWalletFile(
            String password, org.web3j.crypto.ECKeyPair ecKeyPair, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException {

        WalletFile walletFile = null;
        if (useFullScrypt) {
            try {
                walletFile = Wallet.createStandard(password, ecKeyPair);
            } catch (org.web3j.crypto.CipherException e) {
                e.printStackTrace();
            }
        } else {
            try {
                walletFile = Wallet.createLight(password, ecKeyPair);
            } catch (org.web3j.crypto.CipherException e) {
                e.printStackTrace();
            }
        }

        String fileName = getWalletFileName(walletFile);
        File destination = new File(destinationDirectory, fileName);

        objectMapper.writeValue(destination, walletFile);

        return fileName;
    }

    /**
     * Generates a BIP-39 compatible Tpchain wallet. The private key for the wallet can be
     * calculated using following algorithm:
     *
     * <pre>
     *     Key = SHA-256(BIP_39_SEED(mnemonic, password))
     * </pre>
     *
     * @param password Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param destinationDirectory The directory containing the wallet
     * @return A BIP-39 compatible Tpchain wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException if the destination cannot be written to
     */
    public static Bip39Wallet generateBip39Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        org.web3j.crypto.ECKeyPair privateKey = org.web3j.crypto.ECKeyPair.create(sha256(seed));

        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }

    /**
     * Generates a BIP-39 compatible Tpchain wallet using a mnemonic passed as argument.
     *
     * @param password Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param mnemonic The mnemonic that will be used to generate the seed
     * @param destinationDirectory The directory containing the wallet
     * @return A BIP-39 compatible Tpchain wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException if the destination cannot be written to
     */
    public static Bip39Wallet generateBip39WalletFromMnemonic(
            String password, String mnemonic, File destinationDirectory)
            throws CipherException, IOException {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        org.web3j.crypto.ECKeyPair privateKey = org.web3j.crypto.ECKeyPair.create(sha256(seed));

        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }

    public static org.web3j.crypto.Credentials loadCredentials(String password, String source)
            throws IOException, CipherException {
        return loadCredentials(password, new File(source));
    }

    public static org.web3j.crypto.Credentials loadCredentials(String password, File source)
            throws IOException, CipherException {
        WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
        try {
            return org.web3j.crypto.Credentials.create(Wallet.decrypt(password, walletFile));
        } catch (org.web3j.crypto.CipherException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Credentials loadBip39Credentials(String password, String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        return Credentials.create(ECKeyPair.create(sha256(seed)));
    }

    /**
     * Load credentials from JSON wallet string.
     *
     * @param password - password to decrypt JSON wallet string
     * @param content - JSON wallet content string
     * @return Tpchain credentials
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error)
     *     occurs
     */
    public static org.web3j.crypto.Credentials loadJsonCredentials(String password, String content)
            throws IOException, CipherException {
        WalletFile walletFile = objectMapper.readValue(content, WalletFile.class);
        try {
            return org.web3j.crypto.Credentials.create(Wallet.decrypt(password, walletFile));
        } catch (org.web3j.crypto.CipherException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getWalletFileName(WalletFile walletFile) {
        DateTimeFormatter format =
                DateTimeFormatter.ofPattern("'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        return now.format(format) + walletFile.getAddress() + ".json";
    }

    public static String getDefaultKeyDirectory() {
        return getDefaultKeyDirectory(System.getProperty("os.name"));
    }

    static String getDefaultKeyDirectory(String osName1) {
        String osName = osName1.toLowerCase();

        if (osName.startsWith("mac")) {
            return String.format(
                    "%s%sLibrary%sTpchain",
                    System.getProperty("user.home"), File.separator, File.separator);
        } else if (osName.startsWith("win")) {
            return String.format("%s%sTpchain", System.getenv("APPDATA"), File.separator);
        } else {
            return String.format("%s%s.tpchain", System.getProperty("user.home"), File.separator);
        }
    }

    public static String getTestnetKeyDirectory() {
        return String.format(
                "%s%stestnet%skeystore", getDefaultKeyDirectory(), File.separator, File.separator);
    }

    public static String getMainnetKeyDirectory() {
        return String.format("%s%skeystore", getDefaultKeyDirectory(), File.separator);
    }

    /**
     * Get keystore destination directory for a Rinkeby network.
     *
     * @return a String containing destination directory
     */
    public static String getRinkebyKeyDirectory() {
        return String.format(
                "%s%srinkeby%skeystore", getDefaultKeyDirectory(), File.separator, File.separator);
    }

    public static boolean isValidPrivateKey(String privateKey) {
        String cleanPrivateKey = Numeric.cleanHexPrefix(privateKey);
        return cleanPrivateKey.length() == PRIVATE_KEY_LENGTH_IN_HEX;
    }

    public static boolean isValidAddress(String input) {
        return isValidAddress(input, ADDRESS_LENGTH_IN_HEX);
    }

    public static boolean isValidAddress(String input, int addressLength) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == addressLength;
    }
}
