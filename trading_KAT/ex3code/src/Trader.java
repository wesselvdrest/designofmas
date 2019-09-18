import java.util.*;

public class Trader extends Agent {

	// Variables for Buy & Sell Estimates
	private int		estBuyFruit;
	private int		estSellFruit;
	private int		estBuyMeat;
	private int		estSellMeat;
	private int		estBuyWine;
	private int		estSellWine;
	private int		estBuyDairy;
	private int		estSellDairy;

	// Status Variable
	private String	status;

	// A random number generator.
	static Random	gen	= new Random();

	// Trader Constructor
	public Trader(Scape controller) {
		super(controller, "trader");
		status = "chooseProduct";
		this.setProduct("none");
		estBuyFruit = gen.nextInt(50) + 25;
		estSellFruit = gen.nextInt(50) + 25;
		estBuyMeat = gen.nextInt(50) + 25;
		estSellMeat = gen.nextInt(50) + 25;
		estBuyWine = gen.nextInt(50) + 25;
		estSellWine = gen.nextInt(50) + 25;
		estBuyDairy = gen.nextInt(50) + 25;
		estSellDairy = gen.nextInt(50) + 25;
	}
	
	// The Trader's act function, called once per step, handling all the
	// Trader's behavior.
	public void act() {
		if (messageWaiting)
			handleMessages();
		if (status.equals("chooseProduct"))
			chooseProduct();
		if (status.equals("moveToProducer")) 
			moveToProducer();
		if (status.equals("buyFromProducer")) 
			buy(getProduct());
		if (status.equals("negotiateBuy")) 
			negotiateBuy();
		if (status.equals("moveToRetailer")) 
			moveToRetailer();
		if (status.equals("negotiateSale")) 
			negotiateSale();
		if (status.equals("sellToRetailer")) 
			sell();
	}

	// Choosing a product to start trading in, by evaluating the expected
	// profit, based on the Trader's buy & sell estimates for each product. 
	// Switching to state "moveToProducer" afterwards.
	private void chooseProduct() {
		int fruitEstimate = estSellFruit - estBuyFruit;
		int meatEstimate = estSellMeat - estBuyMeat;
		int wineEstimate = estSellWine - estBuyWine;
		int dairyEstimate = estSellDairy - estBuyDairy;
		int[] priceEstimates = { fruitEstimate, meatEstimate, wineEstimate, dairyEstimate };

		int productNr = -1;
		int profit = -10000;

		for (int i = 0; i < priceEstimates.length; i++) {
			if (priceEstimates[i] > profit) {
				productNr = i;
				profit = priceEstimates[i];
			}
		}

		String product = getProduct(productNr);
		status = "moveToProducer";
		this.setProduct(product);
	}

	// Handling all messages received this step, then emptying the message
	// Vector.
	private void handleMessages() {
		for (Message message : messages) {
			// if (message.content() == Messages.Content.PRICE_IS .......
			
			/*YOU WILL HAVE TO IMPLEMENT THIS YOURSELF*/
			
		}
		messageWaiting = false;
		messages.clear();
	}

	// Calling a movement function to move towards the Producer selling the
	// Trader's current product. Something should happen when the Trader meets 
	// another Agent - the famous YOU WILL HAVE TO IMPLEMENT THIS YOURSELF.
	private void moveToProducer() {
		super.moveToGoal(findXLocProduct(this.getProduct()), findYLocProduct(this.getProduct()));
		Vector<Agent> agentsInRange = super.getAgentsInRange();

		for (Agent agent : agentsInRange)
			if (agent instanceof Producer && agent.getProduct() == getProduct())
				status = "negotiateBuy";
	}

	// Calling a movement function to move towards the Retailer to sell the
	// Trader's current product. Something should happen when the Trader meets 
	// another Agent - the famous YOU WILL HAVE TO IMPLEMENT THIS YOURSELF.
	private void moveToRetailer() {
		super.moveToGoal((int) Math.ceil(scape.xSize / 2), (int) Math.ceil(scape.ySize / 2));
		Vector<Agent> agentsInRange = super.getAgentsInRange();

		/* YOU WILL HAVE TO IMPLEMENT THIS YOURSELF */
	}

	// Negotiating a buy from a Producer.
	private void negotiateBuy() {
		for (Agent agent : getAgentsInRange()) {
			if (agent instanceof Producer)
				agent.deliverMessage(new Message(this, Message.Content.WHAT_IS_PRICE, getProduct()));
		}
	}

	// Negotiating a sale to a Retailer.
	private void negotiateSale() {
		for (Agent agent : getAgentsInRange()) {
			/* YOU WILL HAVE TO IMPLEMENT THIS YOURSELF */
		}
	}

	// Buying a product from a Producer.
	private void buy(String product) {
		this.setProduct(product);
		status = "moveToRetailer";
	}

	// Selling a product to a Retailer.
	private void sell() {
		this.setProduct("none");
		status = "chooseProduct";
	}

	// A utility function to find the "XLocation" of a Producer specified by its
	// product.
	private int findXLocProduct(String product) {
		if (product.equals("fruit") || product.equals("wine")) {
			return 0;
		}

		if (product.equals("meat") || product.equals("dairy")) {
			return scape.xSize - 1;
		}

		return -1000;
	}

	// A utility function to find the "YLocation" of a Producer specified by its
	// product.
	private int findYLocProduct(String product) {
		if (product.equals("fruit") || product.equals("meat")) {
			return 0;
		}

		if (product.equals("wine") || product.equals("dairy")) {
			return scape.ySize - 1;
		}

		return -1000;
	}

	// A utility function linking the products to numbers, used in
	// chooseProduct().
	private String getProduct(int i) {
		switch (i) {
			case 0:
				return "fruit";
			case 1:
				return "meat";
			case 2:
				return "wine";
			case 3:
				return "dairy";
		}
		return null;
	}

	// Allowing the Trader to set its own buyPrices.
	private void setBuyPrice(String product, int price) {
		if (product.equals("fruit"))
			estBuyFruit = price;
		if (product.equals("meat"))
			estBuyMeat = price;
		if (product.equals("wine"))
			estBuyWine = price;
		if (product.equals("dairy"))
			estBuyDairy = price;
	}

	// A public "getBuyPrice" function, only intended for use by the Trader
	// itself, or for statistics purposes.
	// NOT for exchanging information with other agents, and should not be
	// called by them.
	public int getBuyPrice(String product) {
		int price = 0;
		if (product.equals("fruit"))
			price = estBuyFruit;
		if (product.equals("meat"))
			price = estBuyMeat;
		if (product.equals("wine"))
			price = estBuyWine;
		if (product.equals("dairy"))
			price = estBuyDairy;
		return price;
	}

	// A public "getSellPrice" function, only intended for use by the Trader
	// itself, or for statistics purposes.
	// in Scape. NOT for exchanging information with other agents, and should
	// not be called by them.
	public int getSellPrice(String product) {
		int price = 0;
		if (product.equals("fruit"))
			price = estSellFruit;
		if (product.equals("meat"))
			price = estSellMeat;
		if (product.equals("wine"))
			price = estSellWine;
		if (product.equals("dairy"))
			price = estSellDairy;
		return price;
	}

	// Allowing the Trader to set its own sellPrices.
	private void setSellPrice(String product, int price) {
		if (product.equals("fruit"))
			estSellFruit = price;
		if (product.equals("meat"))
			estSellMeat = price;
		if (product.equals("wine"))
			estSellWine = price;
		if (product.equals("dairy"))
			estSellDairy = price;
	}

	// A public functioning returning the Agent's state. Only for statistics and
	// visualisation purposes in Scape,
	// MainPanel and ButtonPanel. NOT for exchanging information with other
	// agents, and should not be called by them.
	public String getState() {
		return status;
	}
}
