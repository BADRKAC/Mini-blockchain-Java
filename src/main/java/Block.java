import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Block {
    private static final Logger logger = Logger.getLogger(Block.class.getName());

    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String data, String previousHash, long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }

    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix)
                .equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
           //Applies Sha256 to a string and returns the result. 
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
        //This will contain hash as hexidecimal
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        //or use this
       /*for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) buffer.append('0');
				buffer.append(hex);
			}*/
        return buffer.toString();
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}

