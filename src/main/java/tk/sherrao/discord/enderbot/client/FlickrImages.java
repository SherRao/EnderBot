package tk.sherrao.discord.enderbot.client;

import java.util.Random;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

import tk.sherrao.discord.enderbot.wrapper.ClientWrapper;
                  
public class FlickrImages {

    private final Client client;

    private Flickr api;
    private PhotosInterface photos;
    private Random random;
    
    public FlickrImages( Client client ) {
        this.client = client;
        
        this.api = new Flickr( ClientWrapper.FLICKR_API_KEY, ClientWrapper.FLICKR_SHARED_SECRET, new REST() );
        this.photos = api.getPhotosInterface();
        
    }
    
    public String getImage( String search ) {
        try {
            SearchParameters query = new SearchParameters(); 
            query.setPrivacyFilter(1);
            query.setMedia( "photos" );
            
            PhotoList<Photo> output = photos.search( query, 200, 1 );
            return output.get( 0 ).getOriginalUrl();
            
        } catch ( FlickrException e ) {  
            return null;
            
        } 
    }

    public Client getClient() {
        return client;
    
    }
    
}
