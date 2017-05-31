import DS from 'ember-data';
import Space from "./space";

export default Space.extend({
  name: DS.attr('string'),
  title: DS.attr('string')
});
