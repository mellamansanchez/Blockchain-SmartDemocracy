package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import contracts.generated.Voting;
import contracts.settings.ContractSettings;
import contracts.settings.Utils;


public class SmartDemocracyApp {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    System.out.println("Connecting to Ethereum Blockchain...");
		Web3j web3 = Web3j.build(new HttpService("http://localhost:7545"));
	    System.out.println("Successfuly connected to Ethereum");

		try {
			// web3_clientVersion returns the current client version.
			Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
			
			//eth_blockNumber returns the number of most recent block.
			EthBlockNumber blockNumber = web3.ethBlockNumber().send();
			
			//eth_gasPrice, returns the current price per gas in wei.
			EthGasPrice gasPrice =  web3.ethGasPrice().send();
			  			  
			// Print result
			System.out.println("Client version: " + clientVersion.getWeb3ClientVersion());
			System.out.println("Block number: " + blockNumber.getBlockNumber());
			System.out.println("Gas price: " + gasPrice.getGasPrice());
			
			
			InputStreamReader r = new InputStreamReader(System.in);  
			BufferedReader br = new BufferedReader(r);  
			  
			System.out.println("\nEnter your private key: ");  
			String privateKey = br.readLine();  
			
			Credentials credentials = ContractSettings.getCredentialsFromPrivateKey(privateKey);
			
			String fileName = "contractAddress.txt";
			File f = new File(fileName);
			Voting contract = null;
			if(f.isFile()) {
				InputStream is = new FileInputStream(fileName); 
				BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
				String contractAddress = buf.readLine(); 
				buf.close();

				contract = Voting.load(
						contractAddress,
		                web3, credentials,
		                new DefaultGasProvider());
				System.out.println("\nContract ("+contract.getContractAddress()+") loaded.");  
			}
			else {
				contract = Voting.deploy(
		                web3, credentials,
		                new DefaultGasProvider()).send();
				
				System.out.println("\nContract ("+contract.getContractAddress()+") deployed.");  
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			    writer.write(contract.getContractAddress());
			    writer.close();
			}
	
			System.out.println("Contract valid: "+contract.isValid());
			
			if(!contract.doesSenderExist().send()) {
				String ID;
				boolean condition = false;
				System.out.println("\nRegister with your ID: ");  
				do {
					ID = br.readLine();
					byte[] IDInByte = Utils.getByte32FromString(ID);
					
					contract.addNewId(IDInByte).send();
					
					BigInteger status = contract.getStatus().send();
					if(status.equals(new BigInteger("1"))) {
						System.out.println("This address already has an ID assigned to it. Try again: ");  
					} else if(status.equals(new BigInteger("2"))) {
						System.out.println("This ID has already been assigned. Try again: ");  
					} else {
						System.out.println("Successfully registered!");  
						condition = true;
					}
				} while(!condition);
			} else {
				String ID;
				boolean condition;
				System.out.println("\nLog in with your ID: ");  
				do {
					ID = br.readLine();
					byte[] IDInByte = Utils.getByte32FromString(ID);
					
					condition = contract.logIn(IDInByte).send();
					
					if(condition) {
						System.out.println("Log in successful!");  
					} else {
						System.out.println("Incorrect ID. Try again:");  
					}
				} while(!condition);
			}

			String action = "";
			while(!action.equals("exit")) {
				
				String party = "";
				String candidate = "";
				byte[] partyInByte = null;
				byte[] candidateInByte = null;
				BigInteger candidateId = null;
				
				System.out.println("\n\nIntroduce the option:");  
				System.out.println("1) Show candidates and results:");  
				System.out.println("2) Vote for a candidate:");  
				System.out.println("3) Add candidate(only contract owner):");  
				System.out.println("(Type exit to quit the app)");
				action = br.readLine();

				switch (action) {
					case "1":
						int numCandidates = contract.getNumOfCandidates().send().intValue();
						if(numCandidates == 0) {
							System.out.println("There aren't any candidate added yet");
							break;
						}
						System.out.println("Current candidates:");

						for(int i = 0; i < numCandidates; i++) {
							Tuple3<BigInteger, byte[], byte[]> tuple = contract.getCandidate(new BigInteger(String.valueOf(i))).send();
							candidateId = tuple.getValue1();
							candidateInByte = tuple.getValue2();
							partyInByte = tuple.getValue3();
							candidate = new String(candidateInByte);
							party = new String(partyInByte);
							BigInteger numVotes = contract.totalVotes(candidateId).send();
							System.out.println(candidateId+") Candidate "+candidate.trim()+" from "+party.trim()+" party and with "+numVotes+" votes.");
						}
						break;
					case "2":
						System.out.println("\nIntroduce the name of the party you want to vote:");
						party = br.readLine();
						System.out.println("Introduce the name of the candidate:");
						candidate = br.readLine();
						
						partyInByte = Utils.getByte32FromString(party);
						candidateInByte = Utils.getByte32FromString(candidate);
						candidateId = contract.getCandidateId(partyInByte, candidateInByte).send();
						
						if(candidateId.equals(new BigInteger("-1"))) {
							System.out.println("Invalid candidate or party.");
							break;
						}
						if(contract.hasUserVoted().send()) {
							System.out.println("You have already voted another candidate.");
							break;
						}
						
						byte[] uid = contract.getId().send();
						contract.vote(uid, candidateId).send();
						System.out.println("You have succesfully voted "+candidate+" from "+party+".");
						break;
					case "3":
						System.out.println("\nIntroduce the name of the party you want to create:");
						party = br.readLine();
						System.out.println("Introduce the name of the candidate:");
						candidate = br.readLine();
						
						partyInByte = Utils.getByte32FromString(party);
						candidateInByte = Utils.getByte32FromString(candidate);
						candidateId = contract.getCandidateId(partyInByte, candidateInByte).send();
						
						if(!candidateId.equals(new BigInteger("-1"))) {
							System.out.println("This candidate already exists.");
							break;
						}
						
						try {
							contract.addCandidate(candidateInByte, partyInByte).send();
						} catch (RuntimeException e) {
							System.out.println("The current user is not the owner of the contract and can't add a new candidate");
						}
						break;
					case "exit":
						System.out.println("\nApp exited succesfully.");
						return; 
					default:
						System.out.println("\nUnknown command.");
						break;
				}
			}

		} catch(IOException ex) {
	        throw new RuntimeException("Error whilst sending json-rpc requests", ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }		
}
