package org.jabref.model.entry;


import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.FieldProperty;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This is an immutable class that keeps information regarding single author. It is just a container for the information, with very simple methods to access it.
 * <p>
 * Current usage: only methods <code>getLastOnly</code>, <code>getFirstLast</code>, and <code>getLastFirst</code> are used; all other methods are provided for completeness.
 */
public class User implements Field {

    /**
     * Object indicating the <code>others</code> author. This is a BibTeX feature mostly rendered in "et al." in LaTeX.
     * Example: <code>authors = {Oliver Kopp and others}</code>. This is then appearing as "Oliver Kopp et al.".
     * In the context of BibTeX key generation, this is "Kopp+" (<code>+</code> for "et al.") and not "KO".
     */

    private static final Set<FieldProperty> PROPERTIES = EnumSet.of(FieldProperty.COMMENT, FieldProperty.MULTILINE_TEXT, FieldProperty.VERBATIM, FieldProperty.MARKDOWN);
    public static final User OTHERS = new User("", "");
    private final String name;
    private final String userName;
    private final String userNote;

    /**
     * Creates the Author object. If any part of the name is absent, <CODE>null</CODE> must be passed; otherwise other methods may return erroneous results.
     * <p>
     * In case only the last part is passed, enclosing braces are
     *
     * @param userName     the first name of the author (may consist of several tokens, like "Charles Louis Xavier Joseph" in "Charles Louis Xavier Joseph de la Vall{\'e}e Poussin")
     * @param userNote the abbreviated first name of the author (may consist of several tokens, like "C. L. X. J." in "Charles Louis Xavier Joseph de la Vall{\'e}e Poussin"). It is a responsibility of the caller to create a reasonable abbreviation of the first name.
     */
    public User(String userName, String userNote) {
        this.name = "userNote-" + userName;
        this.userName = userName;
        this.userNote = userNote;
    }


    @Override
    public int hashCode() {
        return Objects.hash(userName, userNote);
    }

    /**
     * Compare this object with the given one.
     *
     * @return `true` iff the other object is an Author and all fields are `Objects.equals`.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof User that) {
            return Objects.equals(userName, that.userName)
                    && Objects.equals(userNote, that.userNote);
        }

        return false;
    }

    /**
     * Returns the first name of the author stored in this object ("First").
     *
     * @return first name of the author (may consist of several tokens)
     */
    public Optional<String> getUserName() {
        return Optional.ofNullable(userName);
    }

    public Optional<String> getUserNote() {
        return Optional.ofNullable(userNote);
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Author{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", userNote='").append(userNote).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Set<FieldProperty> getProperties() {
        return PROPERTIES;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isStandardField() {
        return false;
    }
}
