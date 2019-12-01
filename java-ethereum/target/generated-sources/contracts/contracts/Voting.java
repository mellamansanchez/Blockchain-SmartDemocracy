package contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class Voting extends Contract {
    private static final String BINARY = "6080604052600460095534801561001557600080fd5b5060006001819055600281905560038190558054600160a060020a031916331790556107fe806100466000396000f3006080604052600436106100e55763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166319e6e15881146100ea5780631c0f83741461011457806335b8e8201461012f578063402896a41461016557806342948e181461018e5780634e69d560146101cc5780635d1ca631146101e157806365fc783c146101f657806397371c041461020b5780639ef1204c14610220578063a1adbb251461023d578063aab0190714610258578063c9d2584e14610270578063d32290461461029e578063e19e38c0146102b6578063e8685ba1146102ce575b600080fd5b3480156100f657600080fd5b506101026004356102e3565b60408051918252519081900360200190f35b34801561012057600080fd5b50610102600435602435610321565b34801561013b57600080fd5b5061014760043561037c565b60408051938452602084019290925282820152519081900360600190f35b34801561017157600080fd5b5061017a610399565b604080519115158252519081900360200190f35b34801561019a57600080fd5b506101a36103a9565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156101d857600080fd5b506101026103ad565b3480156101ed57600080fd5b506101026103b3565b34801561020257600080fd5b506101026103c6565b34801561021757600080fd5b5061017a6103cc565b34801561022c57600080fd5b5061023b60043560243561042b565b005b34801561024957600080fd5b5061023b6004356024356104e4565b34801561026457600080fd5b5061017a6004356105f6565b34801561027c57600080fd5b5061017a73ffffffffffffffffffffffffffffffffffffffff6004351661066c565b3480156102aa57600080fd5b5061017a6004356106c5565b3480156102c257600080fd5b5061017a6004356107b8565b3480156102da57600080fd5b506101026107cc565b600080805b60025481101561031a57600081815260056020526040902060010154841415610312576001909101905b6001016102e8565b5092915050565b6000805b600154811015610371576000818152600460205260409020600101548414801561035c575060008181526004602052604090205483145b156103695780915061031a565b600101610325565b506000199392505050565b600081815260046020526040902080546001909101549192909190565b60006103a43361066c565b905090565b3390565b60095490565b3360009081526006602052604090205490565b60025490565b6000805b6007548110156104225760078054829081106103e857fe5b60009182526020909120015473ffffffffffffffffffffffffffffffffffffffff1633141561041a5760019150610427565b6001016103d0565b600091505b5090565b60006104356103cc565b15156104df5760008281526004602052604090206002015460ff161515600114156104df57506002805460018082019092556040805180820182528581526020808201868152600085815260059092529281209151825591519084015560078054938401815590527fa66cc928b5edb82af9bd49922954155ab7b0942694bea4ce44661d9a8736c688909101805473ffffffffffffffffffffffffffffffffffffffff1916331790555b505050565b6000805473ffffffffffffffffffffffffffffffffffffffff16331461056b57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601360248201527f53656e64657220657175616c73206f776e657200000000000000000000000000604482015290519081900360640190fd5b506001805480820182556040805160608101825285815260208082018681528284018681526000868152600484528590209351845590519583019590955593516002909101805460ff19169115159190911790558051828152905191927fec542c373a064661ffef02d633752debf28f3e6c9f253680822d9ccf8b47aec692918290030190a1505050565b6000805b600354811015610661576006600060088381548110151561061757fe5b600091825260208083209091015473ffffffffffffffffffffffffffffffffffffffff1683528201929092526040019020548314156106595760019150610666565b6001016105fa565b600091505b50919050565b6000805b60085481101561066157600880548290811061068857fe5b60009182526020909120015473ffffffffffffffffffffffffffffffffffffffff848116911614156106bd5760019150610666565b600101610670565b60006106d03361066c565b1515600114156106e75750600160095560006107b3565b6106f0826105f6565b1515600114156107075750600260095560006107b3565b6008805460018181019092557ff3f7a9fe364faab93b216da50a3214154f22a0a2b415b23a84c8169e8b636ee301805473ffffffffffffffffffffffffffffffffffffffff19163390811790915560008181526006602090815260408083208790556003805490950190945560099190915582518581529081019190915281517fde9a0f55a2daf87afc99d265e19e9fd0c3c2702aa70b593d036caf29e7ab47a3929181900390910190a15b919050565b336000908152600660205260409020541490565b600154905600a165627a7a7230582095cd8eb5a62afb5cab75e397a0c33f423f347dcb40b2f3c8ff1bf21a6ebd86e10029";

    public static final String FUNC_TOTALVOTES = "totalVotes";

    public static final String FUNC_GETCANDIDATEID = "getCandidateId";

    public static final String FUNC_GETCANDIDATE = "getCandidate";

    public static final String FUNC_DOESSENDEREXIST = "doesSenderExist";

    public static final String FUNC_GETSENDERADDRESS = "getSenderAddress";

    public static final String FUNC_GETSTATUS = "getStatus";

    public static final String FUNC_GETID = "getId";

    public static final String FUNC_GETNUMOFVOTERS = "getNumOfVoters";

    public static final String FUNC_HASUSERVOTED = "hasUserVoted";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_ADDCANDIDATE = "addCandidate";

    public static final String FUNC_DOESIDUSEREXIST = "doesIdUserExist";

    public static final String FUNC_DOESADDRESSEXIST = "doesAddressExist";

    public static final String FUNC_ADDNEWID = "addNewId";

    public static final String FUNC_LOGIN = "logIn";

    public static final String FUNC_GETNUMOFCANDIDATES = "getNumOfCandidates";

    public static final Event ADDEDCANDIDATE_EVENT = new Event("AddedCandidate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event ADDEDID_EVENT = new Event("AddedId", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> totalVotes(BigInteger candidateID) {
        final Function function = new Function(FUNC_TOTALVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(candidateID)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getCandidateId(byte[] party, byte[] candidateName) {
        final Function function = new Function(FUNC_GETCANDIDATEID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(party), 
                new org.web3j.abi.datatypes.generated.Bytes32(candidateName)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple3<BigInteger, byte[], byte[]>> getCandidate(BigInteger candidateID) {
        final Function function = new Function(FUNC_GETCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(candidateID)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple3<BigInteger, byte[], byte[]>>(
                new Callable<Tuple3<BigInteger, byte[], byte[]>>() {
                    @Override
                    public Tuple3<BigInteger, byte[], byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, byte[], byte[]>(
                                (BigInteger) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<Boolean> doesSenderExist() {
        final Function function = new Function(FUNC_DOESSENDEREXIST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> getSenderAddress() {
        final Function function = new Function(FUNC_GETSENDERADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getStatus() {
        final Function function = new Function(FUNC_GETSTATUS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<byte[]> getId() {
        final Function function = new Function(FUNC_GETID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<BigInteger> getNumOfVoters() {
        final Function function = new Function(FUNC_GETNUMOFVOTERS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> hasUserVoted() {
        final Function function = new Function(FUNC_HASUSERVOTED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> vote(byte[] uid, BigInteger candidateID) {
        final Function function = new Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(uid), 
                new org.web3j.abi.datatypes.generated.Uint256(candidateID)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addCandidate(byte[] name, byte[] party) {
        final Function function = new Function(
                FUNC_ADDCANDIDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(name), 
                new org.web3j.abi.datatypes.generated.Bytes32(party)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> doesIdUserExist(byte[] idU) {
        final Function function = new Function(FUNC_DOESIDUSEREXIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(idU)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> doesAddressExist(String addU) {
        final Function function = new Function(FUNC_DOESADDRESSEXIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(addU)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> addNewId(byte[] idU) {
        final Function function = new Function(
                FUNC_ADDNEWID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(idU)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> logIn(byte[] idU) {
        final Function function = new Function(FUNC_LOGIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(idU)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> getNumOfCandidates() {
        final Function function = new Function(FUNC_GETNUMOFCANDIDATES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<AddedCandidateEventResponse> getAddedCandidateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDCANDIDATE_EVENT, transactionReceipt);
        ArrayList<AddedCandidateEventResponse> responses = new ArrayList<AddedCandidateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddedCandidateEventResponse typedResponse = new AddedCandidateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.candidateID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddedCandidateEventResponse> addedCandidateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddedCandidateEventResponse>() {
            @Override
            public AddedCandidateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDCANDIDATE_EVENT, log);
                AddedCandidateEventResponse typedResponse = new AddedCandidateEventResponse();
                typedResponse.log = log;
                typedResponse.candidateID = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddedCandidateEventResponse> addedCandidateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDCANDIDATE_EVENT));
        return addedCandidateEventFlowable(filter);
    }

    public List<AddedIdEventResponse> getAddedIdEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDID_EVENT, transactionReceipt);
        ArrayList<AddedIdEventResponse> responses = new ArrayList<AddedIdEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddedIdEventResponse typedResponse = new AddedIdEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.addressUser = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddedIdEventResponse> addedIdEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddedIdEventResponse>() {
            @Override
            public AddedIdEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDID_EVENT, log);
                AddedIdEventResponse typedResponse = new AddedIdEventResponse();
                typedResponse.log = log;
                typedResponse.id = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.addressUser = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddedIdEventResponse> addedIdEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDID_EVENT));
        return addedIdEventFlowable(filter);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class AddedCandidateEventResponse {
        public Log log;

        public BigInteger candidateID;
    }

    public static class AddedIdEventResponse {
        public Log log;

        public byte[] id;

        public String addressUser;
    }
}
