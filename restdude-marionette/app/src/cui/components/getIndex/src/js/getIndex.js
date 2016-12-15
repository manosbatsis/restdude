define(['kind'], function (kind) {
    // Private method namespace
    var priv = {};

    ///////////////////
    // Public method //
    ///////////////////

    /**
     * Validates the input and calls a helper method to determine the item's index in the array
     *
     * @param   {Array}   array   The array of objects to search within (required)
     * @param   {Mixed}   value   The value of the property to look for (required)
     * @param   {String}  prop    Name of the property to check in each object (optional; defaults to `id`)
     *
     * @return  {Number}          Array index of the matching item, or `-1` if the arguments were invalid
     */
    var _getIndex = function _getIndex (array, value, prop) {
        // Verify parameters

        // Only arrays are supported at this time
        if (kind(array) !== 'array' || array.length === 0) {
            return -1;
        }

        // Fill in the optional parameter
        if (typeof prop !== 'string' || prop.length === 0) {
            prop = 'id';
        }

        return priv.getArrayIndex(array, value, prop);
    };

    /////////////////////
    // Private methods //
    /////////////////////

    /**
     * Returns the index of an object in an array based on a value inside the object
     *
     * @param   {Array}   array   The array of objects to search within (required)
     * @param   {Mixed}   value   The value of the property to look for (required)
     * @param   {String}  prop    Name of the property to check in each object (required)
     *
     * @return  {Number}          Array index of the matching item
     */
    priv.getArrayIndex = function _getArrayIndex (array, value, prop) {
        return array
                .map(
                    function _getArrayIndex_map (item) {
                        return item[prop];
                    }
                )
                .indexOf(value);
    };

    /////////////////////
    // Expose publicly //
    /////////////////////

    return _getIndex;
});
