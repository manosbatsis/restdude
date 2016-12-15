define([], function () {
    // Private method namespace
    var priv = {};

    ///////////////////
    // Public method //
    ///////////////////

    /**
     * Generates a random ID, or ensures an element has an ID and then returns it
     *
     * This may be used to get an element's ID while at the same time ensuring it has one. If the element does not have an ID it will be assigned one that is unique within the document.
     *
     * @param   {Object}  elem  Optional DOM element or other object
     *
     * @return  {String}        Four random alphanumeric characters
     */
    var _guid = function _guid (elem) {
        var id;

        // An element was provided
        if (elem) {
            // See if the element already has an ID
            if (elem.id && typeof elem.id === 'string') {
                return elem.id;
            }

            // Randomly generate an ID
            id = priv.stringOf4RandChars();

            // Try again if this ID is already in use
            if (document.getElementById(id)) {
                id = priv.stringOf4RandChars();
            }

            // Assign the ID to the element
            if (typeof elem === 'object') {
                elem.id = id;
            }

            return id;
        }
        // No element given, just return a random ID
        else {
            return priv.stringOf4RandChars();
        }
    };

    /////////////////////
    // Private methods //
    /////////////////////

    /**
     * Generates a string of four random numbers and letters
     *
     * @return  {String}  Four-character string
     */
    priv.stringOf4RandChars = function _stringOf4RandChars () {
        return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
    };

    /////////////////////
    // Expose publicly //
    /////////////////////

    return _guid;
});
