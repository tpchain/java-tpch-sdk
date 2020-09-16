package cn.com.itcast;

import org.tpc.crypto.WalletUtils;
import org.web3j.crypto.CipherException;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class WalletTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException, CipherException {
        String walletFileName = WalletUtils.generateNewWalletFile("123456",new File("./"),false);
        System.out.println(walletFileName);
    }
}
