import xhttp from 'xhttp';

export default class Search {
    constructor(params) {
        this.elememt = params.elememt;
        this.command = params.command;
        this.wrapperGenerated = false;
        this.submitButton = this.elememt.parentElement.previousElementSibling;
        this.submitButton.disabled = true;

        this.formParams = params.form();
    }

    bind() {
        this.elememt.addEventListener('keyup', (event) => this.makeAjax(event));
        document.addEventListener('click', (event) => this.close(event));
    }

    close() {
        if (this.wrapper) {
            this.wrapper.className = this.wrapper.className.split(' ng-cloak')[0] + ' ng-cloak';
        }
    }

    generateWrapper() {
        const parentElement = this.elememt.parentElement;
        const wrapper = document.createElement('div');
        wrapper.className = 'search-bar--suggestion-dropdown';

        this.ul = document.createElement('ul');
        this.ul.className = 'search-bar__suggestion-list';

        wrapper.appendChild(this.ul);

        parentElement.parentNode.insertBefore(wrapper, parentElement.nextSibling);

        this.wrapper = wrapper;

        this.wrapperGenerated = true;
    }

    createSuggestions(data) {
        if (data.length === 0) {
            this.close();
            return;
        }

        let list = '';

        if (this.wrapperGenerated === false) {
            this.generateWrapper();
        } else {
            this.wrapper.className = this.wrapper.className.split(' ng-cloak')[0];
            this.ul.innerHTML = '';
        }
        let rows = data.length < 10 ? data.length : 10;

        for (var x = 0; x < rows; x++) {
            list = document.createElement('li');
            list.innerHTML = data[x];

            list.addEventListener('click', (event) => this.selectSuggestion(event));
            this.ul.appendChild(list);
        }
    }

    selectSuggestion(event) {
        this.elememt.value = event.target.innerHTML;
        document.getElementsByClassName('btn__icon--search-container')[0].focus();
        return false;
    }

    enableSubmit() {
        this.submitButton.className += ' search-bar--submit__active';
        this.submitButton.disabled = false;
    }

    disableSubmit() {
        this.submitButton.className = this.submitButton.className.split(' search-bar--submit__active')[0];
        this.submitButton.disabled = true;
    }

    makeAjax(event) {
        if (event.shiftKey === false && event.target.value.length > 1) {
            this.enableSubmit();
            this.formParams.q = event.target.value;

            xhttp({
                url: this.command,
                method: 'get',
                params: this.formParams
            })
            // Success handler
            .then((data) => this.createSuggestions(data))
            // Failure handler
            .catch(
                // console
            );
        } else if (event.target.value.length) {
            this.disableSubmit();
            this.close();
        }
    }
}