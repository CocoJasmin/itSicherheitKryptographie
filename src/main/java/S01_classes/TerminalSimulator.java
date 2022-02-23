package S01_classes;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;

public class TerminalSimulator {
        public static void writeInTerminal(String terminalInput)
        {
            try
            {
                Robot robot = new Robot();
                byte[] inputLetters = terminalInput.getBytes(StandardCharsets.UTF_8);
                for (byte letter : inputLetters)
                {
                    int letterAsASCII = letter;
                    if (letterAsASCII > 96 && letterAsASCII < 123) letterAsASCII = letterAsASCII - 32;
                    robot.delay(40);
                  robot.keyPress(letterAsASCII);
                    robot.keyRelease(letterAsASCII);
                }
            }
            catch(AWTException e)
            {
                System.err.println(e);
            }
    }
}