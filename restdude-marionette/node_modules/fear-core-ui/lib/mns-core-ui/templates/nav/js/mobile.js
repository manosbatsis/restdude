const SCROLL_INTO_VIEW_DELAY = 160; // this needs to correspond the the CSS animation timing

export default class MobileNav {

    constructor(windowReference = window) {

        this.window = windowReference;
        this.lastTimeout = null;

        window.addEventListener('resize', function () {
            loadMobileJS.call(MobileNav);
        });

        function loadMobileJS() {
            if (window.innerWidth < 900) {
                this.bind();
            }
        }

        loadMobileJS.call(this);
    }

    bind() {
        if (this.window.document.getElementsByClassName) {
            let checkboxes = this.window.document.getElementsByClassName('menu__mobile-level3');

            for (let iCheckbox = 0; iCheckbox < checkboxes.length; iCheckbox++) {
                checkboxes[iCheckbox].onchange = (e) => {
                    this.level3Change(e);
                };
            }

            let radioButtons = this.window.document.getElementsByName('menu');

            for (let iRadio = 0; iRadio < radioButtons.length; iRadio++) {
                radioButtons[iRadio].onchange = (e) => {
                    this.menuChange(e);
                };
            }
        }
    }

    isElementVisible(el) {

        let rect = el.getBoundingClientRect();

        return (
            rect.top >= 0 &&
            rect.left >= 0 &&
            rect.bottom <= (this.window.innerHeight || this.window.document.documentElement.clientHeight) &&
            rect.right <= (this.window.innerWidth || this.window.document.documentElement.clientWidth)
        );
    }

    scrollCurrentLevel3IntoView(level3Id) {
        this.window.clearTimeout(this.lastTimeout);

        this.lastTimeout = this.window.setTimeout(() => {
            let label = this.window.document.querySelector('[for=' + level3Id + ']');

            if (!this.isElementVisible(label)) {
                label.scrollIntoView();
            }
        }, SCROLL_INTO_VIEW_DELAY);
    }

    deSelectLevel3Checkboxes(selectedCheckboxId) {
        let checkboxes = this.window.document.querySelectorAll('.menu__mobile-level3:checked:not(#' + selectedCheckboxId + ')');

        for (let i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = false;
        }
    }

    level3Change(e) {
        this.deSelectLevel3Checkboxes(e.target.id);
        this.scrollCurrentLevel3IntoView(e.target.id);
    }

    menuChange(e) {
        this.deSelectLevel3Checkboxes(e.target.id);
    }
}
