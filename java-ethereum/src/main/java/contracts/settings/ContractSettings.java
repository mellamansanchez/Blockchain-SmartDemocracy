package contracts.settings;

import org.web3j.crypto.Credentials;

public class ContractSettings {

	private final static String PRIVATE_KEY = "99536bf30dba2932fd43ffb34ba2ab89cfbece7f8c660c319ad94f1ddfc08249";
	
	//public final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
	//public final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
	
	//public final static String RECIPIENT = "0x4Ee89B4D6C48DA20b125349AD148186107C71E32";

	public static Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }
	
	public static Credentials getCredentialsFromPrivateKey(String privateKey) {
        return Credentials.create(privateKey);
    }
}
