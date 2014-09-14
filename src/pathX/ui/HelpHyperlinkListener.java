package pathX.ui;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

/**
 * This class turns the application's help screen into a simple hyperlink 
 * Web browser. When the user clicks on a hyperlink that page is loaded.
 * 
 * @author Richard McKenna
 */
public class HelpHyperlinkListener implements HyperlinkListener
{
    /**
     * This method responds to when the user clicks on a hyperlink
     * in the help screen. When that happens, we load that screen.
     * 
     * @param he This HyperlinkEvent has information about the
     * event, like what the URL was that was clicked so that we
     * can load it.
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent he)
    {
        // NOTE THAT WE CAN RESPOND TO MOUSE-OVER EVENTS
        // HERE AS WELL, BUT WE REALLY ONLY WISH TO RESPOND
        // TO MOUSE CLICKS ON THE HYPERLINK, WHICH IS 
        // AN EventType.ACTIVATED TYPE OF EVENT
        EventType eventType = he.getEventType();
        if (eventType.equals(EventType.ACTIVATED))
        {
            // GET THE LINK AND TELL THE UI TO LOAD IT
            URL link = he.getURL();
            openWebpage(link);
        }
    }    
    
    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    
}