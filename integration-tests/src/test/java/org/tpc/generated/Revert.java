package org.tpc.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.tpc.abi.TypeReference;
import org.tpc.abi.datatypes.Function;
import org.tpc.abi.datatypes.Type;
import org.tpc.abi.datatypes.generated.Uint256;
import org.tpc.crypto.Credentials;
import org.tpc.protocol.Web3j;
import org.tpc.protocol.core.RemoteCall;
import org.tpc.protocol.core.methods.response.TransactionReceipt;
import org.tpc.tx.Contract;
import org.tpc.tx.TransactionManager;
import org.tpc.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.tpc.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.3.0.
 */
public class Revert extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610146806100206000396000f30060806040526004361061004b5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166360fe47b181146100505780636d4ce63c1461006a575b600080fd5b34801561005c57600080fd5b50610068600435610091565b005b34801561007657600080fd5b5061007f610114565b60408051918252519081900360200190f35b600181141561009f57600080fd5b600281141561010f57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601560248201527f54686520726561736f6e20666f72207265766572740000000000000000000000604482015290519081900360640190fd5b600055565b600054905600a165627a7a7230582070aaf3c1fd0da3a5bb0312cac685c5ff0610492739f737ff91a71913b24a80ca0029";

    public static final String FUNC_SET = "set";

    public static final String FUNC_GET = "get";

    @Deprecated
    protected Revert(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Revert(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Revert(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Revert(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> set(BigInteger _number) {
        final Function function = new Function(
                FUNC_SET, 
                Arrays.<Type>asList(new org.tpc.abi.datatypes.generated.Uint256(_number)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Revert load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Revert(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Revert load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Revert(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Revert load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Revert(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Revert load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Revert(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Revert> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Revert.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Revert> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Revert.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Revert> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Revert.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Revert> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Revert.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
