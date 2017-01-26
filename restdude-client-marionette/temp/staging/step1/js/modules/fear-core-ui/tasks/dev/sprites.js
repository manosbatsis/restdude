'use strict';

module.exports = function() {

    var gulp = require('gulp');
    var gulpif = require('gulp-if');
    var sprity = require('sprity');

// generate sprite.png and _sprite.scss
    gulp.task('sprites', function() {
        return sprity.src({
            src: 'app/imagesToSprite/**/*.{png,jpg}',
            style: 'lib/sass/fear-core-ui/_temp_sprites_generated.scss',
            processor: 'fear',
            cssPath: '/',
            split: true,
            margin: 0,
            dimension: [
                {ratio: 1, dpi: 72},
                {ratio: 2, dpi: 192},
                {ratio: 3, dpi: 250}
            ]
        }).pipe(gulpif('*.png', gulp.dest('lib/assets/images/sprites/'), gulp.dest('lib/sass/fear-core-ui/')));

    });
};
