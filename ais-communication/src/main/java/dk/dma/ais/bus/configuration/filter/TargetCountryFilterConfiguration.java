package dk.dma.ais.bus.configuration.filter;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.filter.IPacketFilter;
import dk.dma.ais.filter.TargetCountryFilter;
import dk.dma.enav.model.Country;

@XmlRootElement
public class TargetCountryFilterConfiguration extends FilterConfiguration {
    
    private HashSet<String> allowedCountries = new HashSet<>();
    
    public TargetCountryFilterConfiguration() {
        
    }
    
    @XmlElement(name = "allowed")
    public HashSet<String> getAllowedCountries() {
        return allowedCountries;
    }
    
    public void setAllowedCountries(HashSet<String> allowedCountries) {
        this.allowedCountries = allowedCountries;
    }

    @Override
    public IPacketFilter getInstance() {
        TargetCountryFilter filter = new TargetCountryFilter();
        for (String strCountry : allowedCountries) {
            Country country = Country.getByCode(strCountry);
            if (country == null) {
                throw new IllegalArgumentException("Unknown country: " + strCountry);
            }
            filter.addCountry(country);
        }
        return filter;
    }

}
