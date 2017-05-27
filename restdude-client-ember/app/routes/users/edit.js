import BaseAuthenticated  from '../base-authenticated';
import SaveModelMixin from 'super-rentals/mixins/users/save-model-mixin';

export default BaseAuthenticated.extend(SaveModelMixin, {
  modelTitleProperty: 'username',
});
