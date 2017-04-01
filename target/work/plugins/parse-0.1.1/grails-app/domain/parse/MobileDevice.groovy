package parse

class MobileDevice {

	/*
	 * deviceToken is an Apple-generated string identifier that is required for devices with a deviceType of "ios". It may not be changed once the object is created and must be unique across all of an app's installations.
	 */
	String deviceToken

	/*
	 * Badge is a number field representing the last known application badge for iOS installations.
	 */
	Integer badge

	/*
	 * DeviceType is a required string field that must be set to either "ios" or "android". It may not be changed once the object is created.
	 */
	String deviceType
	/*
	 *  InstallationId is a Parse-generated string identifier that is required for devices with a deviceType of "android" and optional for devices with a deviceType of "ios". It may not be changed once the object is created and must be unique across all of an app's installations.
	 */
	String installationId

	/*
	 * timeZone is a string field representing the system time zone of the device running this installation.
	 */
	String timeZone

	/*
	 * channels is an optional array of strings representing the subscriptions of this installation object.
	 */
	String[] channels

	Date dateCreated // grails will auto timestamp
	Date lastUpdated // grails will auto timestamp
}
