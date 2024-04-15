package org.jabref.gui.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import org.jabref.gui.DialogService;
import org.jabref.gui.JabRefExecutorService;
import org.jabref.gui.LibraryTab;
import org.jabref.gui.StateManager;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.gui.duplicationFinder.DuplicateResolverDialog;
import org.jabref.gui.duplicationFinder.DuplicateResolverDialog.DuplicateResolverResult;
import org.jabref.gui.duplicationFinder.DuplicateResolverDialog.DuplicateResolverType;
import org.jabref.gui.undo.NamedCompound;
import org.jabref.gui.undo.UndoableInsertEntries;
import org.jabref.gui.undo.UndoableRemoveEntries;
import org.jabref.gui.util.BackgroundTask;
import org.jabref.gui.util.DefaultTaskExecutor;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.logic.database.DuplicateCheck;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.database.BibDatabaseMode;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibEntryTypesManager;
import org.jabref.preferences.PreferencesService;

import static org.jabref.gui.actions.ActionHelper.needsDatabase;

public class CalculatePages extends SimpleCommand {

    private final Supplier<LibraryTab> tabSupplier;
    private final DialogService dialogService;
    private final StateManager stateManager;

    private final PreferencesService prefs;
    private final BibEntryTypesManager entryTypesManager;
    private final TaskExecutor taskExecutor;

    public CalculatePages(Supplier<LibraryTab> tabSupplier,
                           DialogService dialogService,
                           StateManager stateManager,
                           PreferencesService prefs,
                           BibEntryTypesManager entryTypesManager,
                           TaskExecutor taskExecutor) {
        this.tabSupplier = tabSupplier;
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        this.prefs = prefs;
        this.entryTypesManager = entryTypesManager;
        this.taskExecutor = taskExecutor;

        this.executable.bind(needsDatabase(stateManager));
    }

    @Override
    public void execute() {
        BibDatabaseContext database = stateManager.getActiveDatabase().orElseThrow(() -> new NullPointerException("Database null"));
        dialogService.notify(Localization.lang("Searching for duplicates..."));

        List<BibEntry> entries = database.getEntries();
        for(BibEntry entry: entries){
            entry.setPageTotal();
        }
        dialogService.notify("Page Total Calculated.");
    }

}

