package org.tasks.injection;

import org.tasks.activities.CalendarSelectionDialog;
import org.tasks.dialogs.AccountSelectionDialog;
import org.tasks.dialogs.AddAttachmentDialog;
import org.tasks.dialogs.ColorPickerDialog;
import org.tasks.dialogs.SortDialog;
import org.tasks.reminders.MissedCallDialog;
import org.tasks.reminders.NotificationDialog;
import org.tasks.reminders.SnoozeDialog;

public interface BaseDialogFragmentComponent {
    void inject(NotificationDialog notificationDialog);

    void inject(MissedCallDialog missedCallDialog);

    void inject(CalendarSelectionDialog calendarSelectionDialog);

    void inject(AddAttachmentDialog addAttachmentDialog);

    void inject(AccountSelectionDialog accountSelectionDialog);

    void inject(SnoozeDialog snoozeDialog);

    void inject(SortDialog sortDialog);

    void inject(ColorPickerDialog colorPickerDialog);
}
