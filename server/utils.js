/**
 * Takes in a generic string
 * Returns the string replaced in such a way that it can be appended to
 * HTML inner text without problems
 */
function toHtml(str) {
	if (str == null) {
		return "";
	}
	return str.replace(/&/g, "&amp;")
			  .replace(/</g, "&lt;")
			  .replace(/>/g, "&gt;")
			  .replace(/\n/g, "<br>")
			  .replace(/"/g, "&quot;")
			  .replace(/'/g, "&"+"#039;");
}
