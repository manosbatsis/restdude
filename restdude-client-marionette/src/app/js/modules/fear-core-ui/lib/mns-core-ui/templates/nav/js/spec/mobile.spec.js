import MobileNav from 'lib/mns-core-ui/templates/nav/js/mobile';

describe('Mobile Nav', () => {

    let windowMock = null;
    let mobileNav = null;

    before(() => {

        windowMock = {
            innerWidth : 1024,
            document: {
                documentElement: {
                    clientHeight: 568,
                    clientWidth: 320
                }
            },
            clearTimeout: () => {
            },
            setTimeout: (func) => {
                func();
            }
        };

        mobileNav = new MobileNav(windowMock);

    });

    it('should show element as visible', () => {

        let elMock = {
            getBoundingClientRect: () => {
                return {
                    top: 10,
                    left: 0,
                    bottom: 40,
                    right: 0
                };
            }
        };

        let result = mobileNav.isElementVisible(elMock);

        expect(result).to.equal(true);
    });


    it('should not show element as visible', () => {

        let elMock = {
            getBoundingClientRect: () => {
                return {
                    top: -10,
                    left: 0,
                    bottom: 20,
                    right: 0
                };
            }
        };

        let result = mobileNav.isElementVisible(elMock);

        expect(result).to.equal(false);
    });

    it('should scroll current level 3 into view', () => {

        let labelMock = {
            scrollIntoView: sinon.spy()
        };

        windowMock.document.querySelector = () => {
            return labelMock;
        };

        windowMock.clearTimeout = () => {
        };

        windowMock.setTimeout = (func) => {
            func();
        };

        let isElementVisibleStub = sinon.stub(mobileNav, 'isElementVisible');
        isElementVisibleStub.returns(false);

        mobileNav.scrollCurrentLevel3IntoView('testId');

        expect(labelMock.scrollIntoView.calledOnce).to.equal(true);

        isElementVisibleStub.restore();
    });

    it('should not scroll level 3 into view', () => {

        let labelMock = {
            scrollIntoView: sinon.spy()
        };

        windowMock.document.querySelector = () => {
            return labelMock;
        };

        windowMock.clearTimeout = () => {
        };

        windowMock.setTimeout = (func) => {
            func();
        };

        let isElementVisibleStub = sinon.stub(mobileNav, 'isElementVisible');
        isElementVisibleStub.returns(true);

        mobileNav.scrollCurrentLevel3IntoView('testId');

        expect(labelMock.scrollIntoView.calledOnce).to.equal(false);

        isElementVisibleStub.restore();
    });

    it('should de select level 3 checkboxes', () => {

        let checkboxes = [
            {checked: true},
            {checked: true}
        ];

        windowMock.document.querySelectorAll = sinon.stub();
        windowMock.document.querySelectorAll.returns(checkboxes);

        let id = 'testId';

        mobileNav.deSelectLevel3Checkboxes(id);

        expect(windowMock.document.querySelectorAll.calledWith('.menu__mobile-level3:checked:not(#' + id + ')')).to.equal(true);
        expect(checkboxes[0].checked).to.equal(false);
        expect(checkboxes[1].checked).to.equal(false);

    });


});