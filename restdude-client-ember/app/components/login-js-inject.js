import Ember from 'ember';

export default Ember.Component.extend({

  didInsertElement() {
    function verticalAlignMiddle()
        {
            var bodyHeight = Ember.$(window).height();
            var formHeight = Ember.$('.vamiddle').height();
            var marginTop = (bodyHeight / 3) - (formHeight / 3);
            if (marginTop > 0)
            {
                Ember.$('.vamiddle').css('margin-top', marginTop);
            }
        }
        Ember.$(document).ready(function()
        {
            verticalAlignMiddle();
        });
        Ember.$(window).bind('resize', verticalAlignMiddle);
  }
});

 
       