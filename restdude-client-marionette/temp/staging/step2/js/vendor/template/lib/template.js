//template.js
//modified version of john resig's micro templating
//http://ejohn.org/blog/javascript-micro-templating/

(function(w){
  var oldRef = w.template;
  var cache = {};

  opts = {
    openTag: '<%',
    closeTag: '%>'
  };

  var template = function tmpl(str, data){
    // Figure out if we're getting a template, or if we need to
    // load the template - and be sure to cache the result.
    var fn = !/\W/.test(str) ?
      cache[str] = cache[str] ||
        tmpl(str) :

      // Generate a reusable function that will serve as a template
      // generator (and which will be cached).
      new Function("obj",
        "var p=[],print=function(){p.push.apply(p,arguments);};" +

        // Introduce the data as local variables using with(){}
        "with(obj){p.push('" +

        // Convert the template into pure JavaScript
        str
          .replace(/[\r\t\n]/g, " ")
          .split(opts.openTag).join("\t")
          .replace(new RegExp("((^|"+opts.closeTag+")[^\t]*)'", 'g'), "$1\r")
          .replace(new RegExp("\t=(.*?)"+opts.closeTag, 'g'), "',$1,'")
          .split("\t").join("');")
          .split(opts.closeTag).join("p.push('")
          .split("\r").join("\\'") + "');}return p.join('');");

    // Provide some basic currying to the user
    return data ? fn( data ) : fn;
  };

  template.options = opts;
  template.noConflict = function() {
    w.template = oldRef;
    return template;
  };

  w.template = template;
})(window);

