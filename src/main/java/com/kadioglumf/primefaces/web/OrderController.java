package com.kadioglumf.primefaces.web;

import com.kadioglumf.primefaces.models.Address;
import com.kadioglumf.primefaces.models.User;
import com.kadioglumf.primefaces.repositories.AddressRepository;
import com.kadioglumf.primefaces.repositories.UserRepository;
import com.kadioglumf.primefaces.utils.AddressType;
import org.eclipse.jdt.internal.compiler.env.IUpdatableModule;
import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named(value = "order")
@ViewScoped
public class OrderController implements Serializable {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User user = new User();

    private Address address = new Address();

    private Address address2 = new Address();

    private Set<Address> addressList = new HashSet<>();

    private Map<String, Map<String,String>> data = new HashMap<String, Map<String,String>>();
    private Map<String,String> countries;
    private Map<String,String> cities;

    private boolean nameValidationStyle = true;
    private boolean phoneValidationStyle = true;
    private boolean postalCodeValidationStyle = true;


    private boolean skip;


    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public Address getAddress2() {
        return address2;
    }

    public void setAddress2(Address address2) {
        this.address2 = address2;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public Map<String, String> getCountries() {
        return countries;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public boolean isNameValidationStyle() {
        return nameValidationStyle;
    }

    public void setNameValidationStyle(boolean nameValidationStyle) {
        this.nameValidationStyle = nameValidationStyle;
    }

    public boolean isPhoneValidationStyle() {
        return phoneValidationStyle;
    }

    public void setPhoneValidationStyle(boolean phoneValidationStyle) {
        this.phoneValidationStyle = phoneValidationStyle;
    }

    public boolean isPostalCodeValidationStyle() {
        return postalCodeValidationStyle;
    }

    public void setPostalCodeValidationStyle(boolean postalCodeValidationStyle) {
        this.postalCodeValidationStyle = postalCodeValidationStyle;
    }

    @PostConstruct
    public void init() {
        countries  = new HashMap<String, String>();
        countries.put("Turkey", "Turkey");
        countries.put("Germany", "Germany");
        countries.put("Brazil", "Brazil");

        Map<String,String> map = new HashMap<String, String>();
        map.put("Istanbul", "Istanbul");
        map.put("İzmir", "İzmir");
        map.put("Ankara", "Ankara");
        data.put("Turkey", map);

        map = new HashMap<String, String>();
        map.put("Berlin", "Berlin");
        map.put("Munich", "Munich");
        map.put("Frankfurt", "Frankfurt");
        data.put("Germany", map);

        map = new HashMap<String, String>();
        map.put("Sao Paulo", "Sao Paulo");
        map.put("Rio de Janerio", "Rio de Janerio");
        map.put("Salvador", "Salvador");
        data.put("Brazil", map);
    }

    public void onCountryChange() {
            if (address.getCountry() != null && !address.getCountry().equals(""))
                cities = data.get(address.getCountry());
            else
                cities = new HashMap<String, String>();

    }

    public void onCountryChangeBilling(){
        if (address2.getCountry() != null && !address2.getCountry().equals(""))
            cities = data.get(address2.getCountry());
        else
            cities = new HashMap<String, String>();
    }

    public void save() throws CloneNotSupportedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);

        if(skip){
            address2 = (Address) address.clone();
        }
        address.setAddressType(AddressType.SHIPPING_ADDRESS);
        address2.setAddressType(AddressType.BILLING_ADDRESS);
        address.setUser(user);
        address2.setUser(user);
        addressList.add(address);
        addressList.add(address2);
        addressRepository.saveAll(addressList);
        info();
    }

    public void info(){
        FacesMessage msg = new FacesMessage("Successful");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }



    public String nameValidation() {
        if (address.getName() == null) {
            nameValidationStyle = false;
            return "";
        }
        if (address.getName().length() < 3) {
            nameValidationStyle = false;
            return "Name is required!";
        } else if (address.getName().length() >= 25) {
            nameValidationStyle = false;
            return "Name can not have more than 25 characters!";
        } else if (characterValidation(address.getName())) {
            nameValidationStyle = false;
            return "Only letters are allowed in the user name!";
        } else {
            nameValidationStyle = true;
            return "";
        }
    }

    public boolean characterValidation(String username) {
        Pattern regex = Pattern.compile("[^a-zA-Z]+");
        Matcher matcher = regex.matcher(username);
        return matcher.find() ? true : false;
    }

    public String phoneValidation() {
        if (address.getPhoneNumber() == null) {
            phoneValidationStyle = false;
            return "";
        }
        if (address.getPhoneNumber().length() != 14) {
            phoneValidationStyle = false;
            return "Phone must be 10 characters!";
        } else {
            phoneValidationStyle = true;
            return "";
        }
    }

    public String postalCodeValidation() {
        if (address.getPostalCode() == 0) {
            postalCodeValidationStyle = false;
            return "";
        }
        if (address.getPhoneNumber().length() > 2) {
            postalCodeValidationStyle = true;
            return "";
        }
        return "";
    }

    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            try {
                save();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }




}
