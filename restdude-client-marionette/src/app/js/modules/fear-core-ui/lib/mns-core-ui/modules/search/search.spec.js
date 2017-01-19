import Search from 'lib/mns-core-ui/modules/search/Search';
import searchMarkup from 'lib/mns-core-ui/modules/search/template.html!text';

describe('Progressive Search control component', function() {

    const mockSearchData = ['jean style', 'jewellery boxes and trinkets', 'jewellery sets', 'jersey tops', 'boys trousers & jeans', 'jeans', 'girls leggings & jeggings', 'royal jelly', 'jersey top', 'jegging', 'jeans'];
    const mockSearchDataSmall = ['jean style', 'jewellery boxes and trinkets'];
    let doc = null;
    let search = null;

    before(function() {
        var d = document.createElement('div');
        System.defaultJSExtensions = true;

        d.innerHTML = searchMarkup;
        doc = d.firstChild;

        const searchBar = doc.getElementsByClassName('search-bar--search')[0];
        const searchCommand = 'searchCommand';

        search = new Search({
            elememt: searchBar,
            command: searchCommand,
            'form': function() {
                const searchForm = searchBar.parentElement.parentElement.parentElement;
                let formValues = {};

                for (var i = 0; i < searchForm.length; i++) {
                    var e = searchForm.elements[i];
                    if (e.name) {
                        formValues[e.name] = e.value;
                    }
                }
                return formValues;
            }
        });

        search.bind();

    });

    it('should disable submit button on initialisation', function() {
        let searchButton = search.elememt.parentElement.previousElementSibling;
        expect(searchButton.hasAttribute('disabled')).to.equal(true);
    });

    it('should create max 10 suggestions when data is more than 10', function() {
        search.createSuggestions(mockSearchData);
        let wrapper = doc.getElementsByClassName('search-bar--suggestion-dropdown')[0];

        expect(wrapper.children[0].children.length).to.equal(10);
    });

    it('should create suggestions equal to data length when data is less than 10', function() {
        search.createSuggestions(mockSearchDataSmall);
        let wrapper = doc.getElementsByClassName('search-bar--suggestion-dropdown')[0];

        expect(wrapper.children[0].children.length).to.equal(mockSearchDataSmall.length);
    });

    var evt = document.createEvent('KeyboardEvent');
    evt.initKeyboardEvent('keyup', true, true);

    it('should enable submit button if input length is greater than 1', function() {
        search.elememt.value = 'Jeans';

        // trigger event
        search.elememt.dispatchEvent(evt);
        let searchButton = search.elememt.parentElement.previousElementSibling;

        expect(searchButton.hasAttribute('disabled')).to.equal(false);
    });

    it('should disable submit button if input length is less than 2', function() {
        search.elememt.value = 'J';

        // trigger event
        search.elememt.dispatchEvent(evt);
        let searchButton = search.elememt.parentElement.previousElementSibling;

        expect(searchButton.hasAttribute('disabled')).to.equal(true);
    });

});