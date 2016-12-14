I downloaded the TTF files from Google at https://github.com/google/fonts/tree/master/apache/opensans/ . These files contain all glyphs/characters (extended Latin, Cyrillic, etc); see `METADATA.json` for details.

I used Font Squirrel to generate the EOT, WOFF, and WOFF2 files. Below is the contents of the `generator_config.txt` file that it spits out so you can perform the generation with the same settings again. Briefly, I kept the TTF files' hinting and did not subset the characters.

One could download the other font formats from Google using http://www.localfont.com however the files were much smaller – I suspect they are only including the standard Latin/Western subset of glyphs.

The TTF fonts can also be downloaded from https://googlefontdirectory.googlecode.com/hg/apache/ including a separate Hebrew set.

(2015-09-08 Craig Patik)


# Font Squirrel Font-face Generator Configuration File
# Upload this file to the generator to recreate the settings
# you used to create these fonts.

{"mode":"expert","formats":["woff","woff2","eotz"],"tt_instructor":"keep","fix_vertical_metrics":"Y","fix_gasp":"xy","add_spaces":"Y","add_hyphens":"Y","fallback":"none","fallback_custom":"100","options_subset":"none","subset_range":["macroman"],"subset_custom":"","subset_custom_range":"","subset_ot_features_list":"","css_stylesheet":"stylesheet.css","filename_suffix":"","emsquare":"2048","spacing_adjustment":"0","rememberme":"Y"}