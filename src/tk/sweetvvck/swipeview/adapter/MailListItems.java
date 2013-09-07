package tk.sweetvvck.swipeview.adapter;

import java.io.Serializable;
import java.util.List;

public class MailListItems implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<MailListItem> MailListItems_data;

	public List<MailListItem> getMailListItems_data() {
		return MailListItems_data;
	}

	public void setMailListItems_data(List<MailListItem> mailListItems_data) {
		MailListItems_data = mailListItems_data;
	}
}
