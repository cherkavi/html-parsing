package com.cherkashin.vitaliy.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class ShellExecute {
	class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	    }
	    
	    public void run()
	    {
	        try
	        {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            while ( (line = br.readLine()) != null)
	                System.out.println(this.type+">  "+line);    
	            } catch (IOException ioe)
	              {
	                ioe.printStackTrace();  
	              }
	    }
	}	
	
	/**  
	 * @param command - Windows command
	 * @return 
	 * <ul>
	 * 	<li><b>0</b> - выполнено </li>
	 * 	<li><b>>0</b> - есть ошибки </li>
	 * </ul>
	 * */
	public static int execute(String command) throws Exception {
		// System.out.println("begin");
		String[] cmd=new String[3];
		cmd[0] = "cmd.exe" ;
        cmd[1] = "/C" ;
        cmd[2] = command;
       
        Runtime rt = Runtime.getRuntime();
        // System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
        Process proc = rt.exec(cmd);
        // any error message?
        // ShellExecute.StreamGobbler errorGobbler = new ShellExecute().new StreamGobbler(proc.getErrorStream(), "ERROR");            
        // any output?
        // ShellExecute.StreamGobbler outputGobbler = new ShellExecute().new StreamGobbler(proc.getInputStream(), "OUTPUT");
            
        // kick them off
        // errorGobbler.start();
        // outputGobbler.start();
                                
        // any error???
        return proc.waitFor();
        // System.out.println("ExitValue: " + exitVal);            
		// System.out.println("-end-");
	}
	
	
}
