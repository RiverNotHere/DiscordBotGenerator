import java.io.*;
import java.util.*;

public class init {
    public static void main(String args[]) throws IOException{
        System.out.printf(
            "\n"+
            "*************************************\n"+
            "******* Discord Bot Generator *******\n"+
            "*************************************\n"+
            "By: o0River0o\nversion: 0.1.0\n"+
            "\n"
            );

        Scanner sc = new Scanner(System.in);

        String path, frame;

        // get bot's path and name(folder name)
        System.out.println("Enter the path you want you bot to generated at:");
        path = sc.nextLine();
        System.out.println("Enter your bot's name(change it later):");
        path = path + sc.nextLine();

        frame = getFramework(sc);

        generateBot(path, frame);

        sc.close();
       
    }

    private static void generateBot(String path, String type) throws IOException{
        System.out.println("Generating your bot...");
        boolean botDir = mkdir(path);
        if(botDir) {
            System.out.println("Installing Node dependencies...");
            installDepens(path);
            System.out.println("Generating bot file...");
            boolean success = addBot(path);
            if(success) {
                System.out.println("Generated your bot successfully!");
                System.out.println(
                    "\n"+
                    "Thanks for using Discord Bot Generator! If you like it, please leave a star to this repository o(^▽^)o\n"+
                    "Report your issue at: https://github.com/o0River0o/DiscordBotGenerator/issues"
                );
            }else{
                throw new IllegalArgumentException("Can't create bot entry, check your settings.");
            }
        }else{
            throw new IllegalArgumentException("Can't create bot directory, check your settings.");
        }
    }

    private static boolean addBot(String path) throws IOException{
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path+"/index.js")));
        PrintWriter env = new PrintWriter(new BufferedWriter(new FileWriter(path+"/.env")));
        PrintWriter inst = new PrintWriter(new BufferedWriter(new FileWriter(path+"/README")));
        try{
            System.out.println("Generating .env...");
            env.print(
                """
                TOKEN = | Your bot token here |
                PREFIX = | Your command prefix here |
                """
            );
            env.close();
            System.out.println(".env file generated");

            System.out.println("Generating index.js...");
            out.print(
                """
                //make changes to fit your needs
                require('dotenv').config();
                const discord = require('discord.js');
                const fs = require('fs').promises;
                const client = new discord.Client();
                const path = require('path');
                
                client.login(process.env.TOKEN); //replace your token in .env file
                
                client.on('ready', () => {
                    console.log(`[INFO] ${client.user.tag} is logged in`);
                })
                
                client.on('message', message => {
                    if(message.author.bot) return;
                    if(!message.content.startsWith(process.env.PREFIX)) return; //change your bot Prefix in .env file
                    let args = message.content.substring(message.content.indexOf(PREFIX)+1).split(new RegExp(" "));
                    let command = args.shift().toLowerCase();
                    
                    if(command === 'ping'){
                        message.channel.send('pong!');
                    }
                })
                """
            );
            out.close();
            
            System.out.println("Generating README...");
            inst.print(
                """
                ----------------
                | Instructions |
                ----------------

                Thank you for using Discord Bot Generator!

                Your bot has been successfully generated, 
                to make it running please go to: https://www.discord.com/developers to create your bot
                replace the placeholder with your bot token in .env file

                You can change your bot's prefix at .env file also.

                Hope you'll develop a wonderful bot!

                Explore more tools at: https://www.github.com/o0River0o

                """
            );
            inst.close();
            System.out.println("index.js generated");

            return true;
        }catch(Exception e) {
            System.err.println("Failed to Generate bot index.");
            e.printStackTrace();
            out.close();
            return false;
        }
        
    }

    private static void installDepens(String path) {
        executeBashCommand("yarn --cwd " + path + " init -y ");
        executeBashCommand("yarn --cwd " + path +" add discord.js dotenv");
        executeBashCommand("yarn --cwd " + path +" add -D nodemon");
    }

    private static boolean executeBashCommand(String command) {
        boolean success = false;
        System.out.println("Executing BASH command:\n" + command);
        Runtime r = Runtime.getRuntime();
        String[] commands = { "bash", "-c", command };
        try {
          Process p = r.exec(commands);
    
          p.waitFor();
          BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line = "";
    
          while ((line = b.readLine()) != null) {
            System.out.println(line);
          }
    
          b.close();
          success = true;
        } catch (Exception e) {
          System.err.println("Failed to execute bash with command: " + command);
          e.printStackTrace();
        }
        return success;
      }

    private static boolean mkdir(String path) {
        System.out.println("Creating bot directory...");

        // Creating a File object
        File file = new File(path);
        // Creating the directory
        boolean bool = file.mkdir();
        if (bool) {
          System.out.println("Directory created successfully");
          return true;
        } else {
          System.out.println("Sorry couldn’t create specified directory");
          return false;
        }
    }

    private static String getFramework(Scanner sc) {

        System.out.println("Please choose your bot's framework: [1] discord.js");
        int fm = sc.nextInt();

        switch (fm) {
            case 1:
                return "node";
            case 2:
                System.out.println("Sorry we don't support that yet.");
                return getFramework(sc);
            default:
                System.out.println("Please Enter a VALID selection");
                return getFramework(sc);
        }

    }
}