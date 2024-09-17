package academy.silicon.resource;

import academy.silicon.model.Entry;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import io.quarkus.logging.Log;
import academy.silicon.service.EntryService;

@CrossOrigin
@Path("/entryResource")
public class EntryResource {
    SimpleDateFormat piggyDateFormatter = new SimpleDateFormat("yyyy-MM-dd+HH:mm");

    @Inject
    EntryService eService;

    @GET
    @Path("/findAll")
    public List<Entry> findAll() {
        System.out.println("findAll");
        return eService.findAll();
    }
    @GET
    @Path("/findByAccountCategory/{accountID}/{category}")
    public List<Entry> findByAccountID(String accountID, String category) {
        return eService.findByAccountIDAndCategory(accountID, category);
    }

    @GET
    @Path("/findByAccount/{accountID}")
    public List<Entry> findByAccountID(String accountID) {
        return eService.findByAccountID(accountID);
    }

    @GET
    @Path("/replaceCategory/{oldCategory}/{newCategory}")
    public List<Entry> replaceCategory(String oldCategory, String newCategory) {
        return eService.replaceCategory(oldCategory, newCategory);
    }

    @GET
    @Path("/findByCategory/{category}")
    public List<Entry> findByCategory(String category) {
        return eService.findByCategory(category);
    }

    @GET
    @Path("/findByAccountAndDates/{accountID}/{dateInit}/{dateEnd}")
    public List<Entry> findByAccountIDAndDates(String accountID, String dateInit, String dateEnd) throws Exception {
        Long di = piggyDateFormatter.parse(dateInit).getTime();
        Long de = piggyDateFormatter.parse(dateEnd).getTime();
        Log.info(di);
        Log.info(de);
        return eService.findByAccountIDAndDates(accountID, di, de);
    }

    @GET
    @Path("/findByTimestamp/{timestamp}")
    public Entry findByTimestamp(Long timestamp) {
        return eService.findByTimestamp(timestamp);
    }


    @POST
    public void addEntry(Entry entry) throws Exception {
        System.out.println(entry);
        if(entry.getTimestamp()==null) {
            entry.setTimestamp(piggyDateFormatter.parse(entry.getDate()).getTime());
        }
        calculateBalance();
        eService.addEntry(entry);
    }

    public void calculateBalance() {
        List<Entry> entries = eService.findAll();
        BigDecimal balance = BigDecimal.ZERO;
        for (Entry entry : entries) {
            balance = balance.add(entry.getAmount());
            entry.setBalance(balance);
        }
    }
        
        
        

    
}
