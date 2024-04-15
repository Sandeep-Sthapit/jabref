package org.jabref.gui.entryeditor;

import java.util.*;
import java.util.stream.Collectors;

import javax.swing.undo.UndoManager;

import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.autocompleter.SuggestionProviders;
import org.jabref.gui.fieldeditors.FieldNameLabel;
import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.theme.ThemeManager;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.logic.journals.JournalAbbreviationRepository;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.pdf.search.IndexingTaskManager;
import org.jabref.logic.util.TestEntry;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.UnknownField;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.field.UserSpecificCommentField;
import org.jabref.model.entry.field.UserSummary;
import org.jabref.preferences.PreferencesService;
import java.io.*;

public class UserTab extends FieldsEditorTab {
    public static final String NAME = "User";
    private SequencedSet<Field> userSummaries = new LinkedHashSet<>();

    public UserTab(PreferencesService preferences,
                       BibDatabaseContext databaseContext,
                       SuggestionProviders suggestionProviders,
                       UndoManager undoManager,
                       DialogService dialogService,
                       StateManager stateManager,
                       ThemeManager themeManager,
                       IndexingTaskManager indexingTaskManager,
                       TaskExecutor taskExecutor,
                       JournalAbbreviationRepository journalAbbreviationRepository) {
        super(
                false,
                databaseContext,
                suggestionProviders,
                undoManager,
                dialogService,
                preferences,
                stateManager,
                themeManager,
                taskExecutor,
                journalAbbreviationRepository,
                indexingTaskManager
        );
        // this could be next task
//        setText(Localization.lang("User"));
        setText(Localization.lang("User"));
        setText("Summary");
        // replace this with summary icon
        setGraphic(IconTheme.JabRefIcons.USER_SUMMARY.getGraphicNode());
//        defaultOwner = "advisor";
////        this.defaultOwner = preferences.getOwnerPreferences().getDefaultOwner().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "-");
//        userSummary = new UserSummary(defaultOwner);
    }

    public void addUserSummaries(Field summary) {
        this.userSummaries.add(summary);
    }

    public SequencedSet<Field> getUserSummaries() {
       return this.userSummaries;
    }

    @Override
    protected SequencedSet<Field> determineFieldsToShow(BibEntry entry) {
        SequencedSet<Field> summaries = getUserSummaries();
        summaries.add(StandardField.USERSUMMARY);
        return summaries;
    }
    private void setCompressedRowLayout() {
        int numberOfComments = gridPane.getRowCount() - 1;
        double totalWeight = numberOfComments * 3 + 1;

        RowConstraints commentConstraint = new RowConstraints();
        commentConstraint.setVgrow(Priority.ALWAYS);
        commentConstraint.setValignment(VPos.TOP);
        double commentHeightPercent = 3.0 / totalWeight * 100.0;
        commentConstraint.setPercentHeight(commentHeightPercent);

        RowConstraints buttonConstraint = new RowConstraints();
        buttonConstraint.setVgrow(Priority.ALWAYS);
        buttonConstraint.setValignment(VPos.TOP);
        double addButtonHeightPercent = 1.0 / totalWeight * 100.0;
        buttonConstraint.setPercentHeight(addButtonHeightPercent);

        ObservableList<RowConstraints> rowConstraints = gridPane.getRowConstraints();
        rowConstraints.clear();
        for (int i = 1; i <= numberOfComments; i++) {
            rowConstraints.add(commentConstraint);
        }
        rowConstraints.add(buttonConstraint);
    }
    @Override
    protected void setupPanel(BibEntry entry, boolean compressed) {
        super.setupPanel(entry, false);
        HBox summaryContainer = new HBox();
        Label userLabel = new Label("Username: ");
        Button addSummary = new Button("Add User Summary");
        TextField userNameField = new TextField();
        userNameField.setMaxWidth(300);
        addSummary.setOnAction(e -> {
            String owner = userNameField.getText();
            UserSummary userSummary = new UserSummary(owner);
            addUserSummaries(userSummary);
            setupPanel(entry, false);
        });
        summaryContainer.getChildren().addAll(userLabel, userNameField, addSummary);
        gridPane.add(summaryContainer, 1, gridPane.getRowCount(), 2, 1);
        setCompressedRowLayout();
    }
}
