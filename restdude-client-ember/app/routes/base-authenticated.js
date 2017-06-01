import Ember from 'ember';
import ApplicationRoute from './application';
import AuthenticatedRouteMixin from 'ember-simple-auth/mixins/authenticated-route-mixin';


export default ApplicationRoute.extend(AuthenticatedRouteMixin,{

});
