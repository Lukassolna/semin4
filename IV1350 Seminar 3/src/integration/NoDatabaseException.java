package integration;

public class NoDatabaseException extends Exception{

	 public NoDatabaseException(String messageForUser){
	        super(messageForUser);
	    }

	    /**
	     * GetError message is used to store the final version of the stacktrace
	     * @return Returns a string with message, stack and current time
	     */
	    public String getErrorMessage(){
	        StringBuilder strBu = new StringBuilder();
	        strBu.append("Database Exception -  " + java.time.LocalDateTime.now()+ "\n");
	        strBu.append(this.getMessage() + "\n");
	        strBu.append(this.getStackTrace()[0].toString());
	        
	        return strBu.toString();
	    }
	}
