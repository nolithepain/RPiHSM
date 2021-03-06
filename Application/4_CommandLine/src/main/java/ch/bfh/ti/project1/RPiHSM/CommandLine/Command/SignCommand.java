package ch.bfh.ti.project1.RPiHSM.CommandLine.Command;

import ch.bfh.ti.project1.RPiHSM.API.SerialHelper;
import ch.bfh.ti.project1.RPiHSM.API.Sign;
import com.beust.jcommander.Parameter;

import javax.naming.OperationNotSupportedException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static ch.bfh.ti.project1.RPiHSM.CommandLine.Utils.Constants.*;

/**
 * <h1>SignCommand</h1>
 * Parses the command line parameters and use the {@link Sign} to create a signature on the Raspberry Pi.
 */
public class SignCommand implements CommandI {


    private SerialHelper serialHelper;
    private ResourceBundle b;
    private String userPath;

    @Parameter(description = PARAMETERS_LIST)//JCommander requirements (all others parameters)
    private List<String> command = new ArrayList<>();

    @Parameter(names = {NAME_COMPLETE_VALUE, NAME_SHORT_VALUE}, description = NAME_VALUE, required = true)
    private String keySetName;

    @Parameter(names = {FILE_COMPLETE_VALUE, FILE_SHORT_VALUE}, description = FILE_SIGN_VALUE, required = true)
    private String filePath;

    /**
     * Saves the userPath and  an instance of the {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI} for future use.
     *
     * @param userPath     user home directory on the Raspberry Pi
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     */
    public SignCommand(String userPath, SerialHelper serialHelper) {
        this.userPath = userPath;
        this.serialHelper = serialHelper;
        this.b = ResourceBundle.getBundle("language", Locale.getDefault());
    }

    /**
     * Creates a signature of a given file with the given key set on the Raspberry Pi using {@link Sign}.
     *
     * @return a message that describes the result of the operation (error or success)
     */
    @Override
    public String execute() {
        Sign s = new Sign(serialHelper, userPath, keySetName, filePath);

        try {
            if (s.sign()) {
                return b.getString("success.sign");
            } else {
                return b.getString("error.sign");
            }
        } catch (OperationNotSupportedException e) {
            return b.getString("error.unsupported.operation");
        } catch (FileNotFoundException e) {
            return b.getString("error.file.not.found");
        }

    }

    /**
     * Returns a message that contains the right command syntax.
     *
     * @return right command syntax
     */
    @Override
    public String print() {
        return SIGN_COMMAND;
    }
}
