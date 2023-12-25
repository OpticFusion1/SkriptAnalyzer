package optic_fusion1.skriptanalyzer.analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import optic_fusion1.kitsune.analyzer.java.file.FileAnalyzer;
import optic_fusion1.skriptanalyzer.parsing.tokenizer.Tokenizer;
import optic_fusion1.skriptanalyzer.parsing.tokenizer.Tokenizer.Token;

// TODO: Handle every valid variation
// TODO: Handle commands
// TODO: Implement proper Parser
public class SkAnalyzer extends FileAnalyzer {

    private static final Tokenizer TOKENIZER = new Tokenizer();

    @Override
    public void analyze(File file) {
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            TOKENIZER.init(content);
            while (TOKENIZER.hasMoreTokens()) {
                Token token = TOKENIZER.getNextToken();
                if (token == null) {
                    System.out.println("Reached EOF");
                    return;
                }
                // Checks for op and deop
                if (isValidToken(token, "IDENTIFIER", "op")) {
                    String value = TOKENIZER.getNextToken().getValue();
                    System.out.println(value.equals("all") ? "Ops all players" : "Ops a player");
                    continue;
                }
                if (isValidToken(token, "IDENTIFIER", "deop")) {
                    String value = TOKENIZER.getNextToken().getValue();
                    System.out.println(value.equals("all") ? "Deops all players" : "Deops a player");
                    continue;
                }

                // Handles banning/unbanning
                if (isValidToken(token, "IDENTIFIER", "unban")) {
                    Token eatenToken = TOKENIZER.getNextToken();
                    System.out.println("Unbans a player");
                    continue;
                }
                if (isValidToken(token, "ip-ban")) {
                    Token eatenToken = TOKENIZER.getNextToken();Token eatenToken2 = TOKENIZER.getNextToken();Token eatenToken3 = TOKENIZER.getNextToken();
                    Token reasonToken = TOKENIZER.getNextToken();
                    String reason = "BANNED";
                    if (isValidToken(reasonToken, "STRING")) {
                        reason = reasonToken.getValue();
                    }
                    System.out.println("IP-bans a player due to " + reason);
                    continue;
                }
                if (isValidToken(token, "IDENTIFIER", "ban")) {
                    Token nextToken = TOKENIZER.getNextToken();
                    if (isValidToken(nextToken, "STRING")) {
                        System.out.println("Bans the IP: " + nextToken.getValue());
                        continue;
                    }
                    Token eatenToken = TOKENIZER.getNextToken();Token eatenToken2 = TOKENIZER.getNextToken();
                    Token reasonToken = TOKENIZER.getNextToken();
                    String reason = "BANNED";
                    if (isValidToken(reasonToken, "STRING")) {
                        reason = reasonToken.getValue();
                    }
                    // TODO: Implement getting ban length
                    System.out.println("Bans a player due to " + reason);
                    continue;
                }
                
                // Handles stopping/restarting the server
                if (isValidToken(token, "IDENTIFIER", "restart")) {
                    Token nextToken = TOKENIZER.getNextToken();
                    if (isValidToken(nextToken, "IDENTIFIER", "server")) {
                        System.out.println("Restarts the server");
                        continue;
                    }
                    continue;
                }
                if (isValidToken(token, "IDENTIFIER", "stop")) {
                    Token eatenToken = TOKENIZER.getNextToken();
                    Token nextToken = TOKENIZER.getNextToken();
                    if (isValidToken(nextToken, "IDENTIFIER", "server")) {
                        System.out.println("Stops the server");
                        continue;
                    }
                }
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(SkAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isValidToken(Token token, String type) {
        return token.getType().equals(type);
    }

    private boolean isValidToken(Token token, String type, String value) {
        return token.getType().equals(type) && token.getValue().equals(value);
    }

}
