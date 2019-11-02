package ca.ubc.cs304.model;

/**
 * Model for a Customer Object
 */
public class CustomerModel {
	private final String dlicense;
	private final int cellphone;
	private final String name;
	private final String address;	
	
	public CustomerModel(String dlicense, int cellphone, String name, String address) {
        this.dlicense = dlicense;
        this.cellphone = cellphone;
		this.name = name;
		this.address = address;
	}

	public String getDlicense() {
		return dlicense;
	}

	public int getCellphone() {
		return cellphone;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}
}
