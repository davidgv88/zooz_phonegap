/**
 *  
 * @return Object literal singleton instance of ZooZCheckout
 */
var ZooZCheckout = function() {
};

/**
  * @param directory The directory for which we want the listing
  * @param successCallback The callback which will be called when directory listing is successful
  * @param failureCallback The callback which will be called when directory listing encouters an error
  */
ZooZCheckout.prototype.pay = function(args, successCallback, failureCallback) {
	console.log("ZooZCheckout pay called.");
	return cordova.exec(successCallback,    //Success callback from the plugin
			failureCallback,     //Error callback from the plugin
			'ZooZCheckoutPlugin',  //Tell PhoneGap to run "ZooZCheckoutPlugin" Plugin
			'pay',              //Tell plugin, which action we want to perform
			[args]);        //Passing list of args to the plugin
};


window.zoozCheckout = new ZooZCheckout();
