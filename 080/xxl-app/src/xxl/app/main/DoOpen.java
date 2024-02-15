package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import xxl.Calculator;
import xxl.exceptions.FileNotSavedException;
import xxl.exceptions.UnavailableFileException;

/**
 * Open existing file.
 */
class DoOpen extends Command<Calculator> {

    DoOpen(Calculator receiver) {
        super(Label.OPEN, receiver);
    }

    @Override
    protected final void execute() throws CommandException {

        try {
            _receiver.isValidSpreadsheet();
        }
        catch (FileNotSavedException e) {
            if (Form.confirm(Prompt.saveBeforeExit())) {
                DoSave saveBeforeExit = new DoSave(_receiver);
                saveBeforeExit.execute();
            }
        }

        try {
            String fileName = Form.requestString(Prompt.openFile());
            _receiver.load(fileName);
        } catch (UnavailableFileException e) {
                throw new FileOpenFailedException(e);
        }
    }
    
}
