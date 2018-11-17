package csc472.depaul.edu.finalproject.models.command;

import csc472.depaul.edu.finalproject.models.command.ICommand;

public abstract class UndoableCommand implements ICommand {
    public abstract void undo();
}
