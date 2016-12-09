require(['tether-orig'], function (Tether) {
    // attach to global scope until bootstrap devs fix their module deps
    window.Tether = Tether;
    console.log("Attached window.Tether: " + window.Tether);
    // it's important to have this, to keep original module definition approach
    return Tether;
});
