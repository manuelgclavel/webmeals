$wnd.com_example_mobile_widgetset_MobileWidgetset.runAsyncCallback9("function GGc(){}\nfunction IGc(){}\nfunction KGc(){}\nfunction xmd(){wWb.call(this)}\nfunction YZb(a,b){return FC(a.G.no(b))}\nfunction WTd(){bWb.call(this);this.I=JGe;this.a=new C5d}\nfunction JQc(c,a){var b=c;a.notifyChildrenOfSizeChange=qbe(function(){b.Rk()})}\nfunction RQc(b){try{b!=null&&eval('{ var document = $doc; var window = $wnd; '+b+'}')}catch(a){}}\nfunction GQc(b){if(b&&b.iLayoutJS){try{b.iLayoutJS();return true}catch(a){return false}}else{return false}}\nfunction FQc(a,b){var c,d;for(c=k1d(new l1d(a.f));c.a.Df();){d=FC(r1d(c));if(MC(a.f.no(d))===MC(b)){return d}}return null}\nfunction KQc(a,b){var c,d;d=FQc(a,b);d!=null&&a.f.qo(d);c=DC(a.a.no(b),498);if(c){a.a.qo(b);return bqb(a,c)}else if(b){return bqb(a,b)}return false}\nfunction HQc(a){var b,c,d;d=(slb(),a._b).getElementsByTagName('IMG');for(b=0;b<d.length;b++){c=d[b];qlb.Be(c,Jfe)}}\nfunction LQc(a,b){var c,d,e;if((Yk(),b).hasAttribute(rCe)){e=bl(b,rCe);a.e.po(e,b);Hk(b,'')}else{d=(slb(),Anb(b));for(c=0;c<d;c++){LQc(a,znb(b,c))}}}\nfunction MQc(a,b,c){var d,e;if(!b){return}d=EC(a.e.no(c));if(!d&&a.d){throw new DZd('No location '+c+' found')}e=DC(a.f.no(c),9);if(e==b){return}!!e&&KQc(a,e);a.d||(d=(slb(),a._b));Tpb(a,b,(slb(),d));a.f.po(c,b)}\nfunction NQc(a,b){var c,d,e;d=b.qh();if(d.$b!=a){return}e=DC(a.a.no(d),498);if(p6b(b.oh())){if(!e){c=FQc(a,d);bqb(a,d);e=new x6b(b,a.b);Spb(a,e,EC(a.e.no(c)));a.a.po(d,e)}k6b(e.a)}else{if(e){c=FQc(a,d);bqb(a,e);Spb(a,d,EC(a.e.no(c)));a.a.qo(d)}}}\nfunction CGc(c){var d={setter:function(a,b){a.a=b},getter:function(a){return a.a}};c.hk(Ucb,FGe,d);var d={setter:function(a,b){a.b=b},getter:function(a){return a.b}};c.hk(Ucb,GGe,d);var d={setter:function(a,b){a.c=b},getter:function(a){return a.c}};c.hk(Ucb,HGe,d)}\nfunction OQc(){var a;cqb.call(this);this.e=new C5d;this.f=new C5d;this.a=new C5d;Pob(this,(slb(),Nm($doc)));a=this._b.style;Jo(a,Mpe,(Po(),jde));Jo(a,Gse,(rs(),Wge));Jo(a,Yse,Wge);(t0b(),!s0b&&(s0b=new L0b),t0b(),s0b).a.i&&Jo(a,Jce,(wr(),Tpe));Fk(this._b,JGe);jpb(this._b,_Ae,true)}\nfunction IQc(a,b,c){var d;b=EQc(a,b);d=v8b(c+'/layouts/');b=K$d(b,'<((?:img)|(?:IMG))\\\\s([^>]*)src=\"((?![a-z]+:)[^/][^\"]+)\"',IGe+d+'$3\"');b=K$d(b,'<((?:img)|(?:IMG))\\\\s([^>]*)src=[^\"]((?![a-z]+:)[^/][^ />]+)[ />]',IGe+d+'$3\"');b=K$d(b,'(<[^>]+style=\"[^\"]*url\\\\()((?![a-z]+:)[^/][^\"]+)(\\\\)[^>]*>)','$1 '+d+'$2 $3');Hk((slb(),a._b),b);a.e.cf();LQc(a,a._b);HQc(a);a.c=zlb(a._b);!a.c&&(a.c=a._b);JQc(a,a.c);a.d=true}\nfunction EQc(a,b){var c,d,e,f,g,h,j,k;b=K$d(b,'_UID_',a.g+'__');a.i='';d=0;f=b.toLowerCase();h='';j=f.indexOf('<script',0);while(j>0){h+=b.substr(d,j-d);j=f.indexOf('>',j);e=f.indexOf('<\\/script>',j);a.i+=b.substr(j+1,e-(j+1))+';';g=d=e+9;j=f.indexOf('<script',g)}h+=V$d(b,d,b.length-d);f=h.toLowerCase();k=f.indexOf('<body');if(k<0){h=h}else{k=f.indexOf('>',k)+1;c=f.indexOf('<\\/body>',k);c>k?(h=h.substr(k,c-k)):(h=V$d(h,k,h.length-k))}return h}\nfunction wmd(a){var b,c,d;if(a.a){return}c=(!a.L&&(a.L=qSb(a)),DC(DC(DC(a.L,6),147),444)).c;b=(!a.L&&(a.L=qSb(a)),DC(DC(DC(a.L,6),147),444)).b;c!=null&&(b=YZb(a.D,'layouts/'+c+'.html'));if(b!=null){IQc((!a.B&&(a.B=YSb(a)),DC(a.B,246)),b,ZZb(a.D))}else{d=c!=null?'Layout file layouts/'+c+'.html is missing.':'Layout file not specified.';Hk(Fob((!a.B&&(a.B=YSb(a)),DC(a.B,246))),'<em>'+d+' Components will be drawn for debug purposes.<\\/em>')}a.a=true}\nvar FGe='childLocations',GGe='templateContents',HGe='templateName',IGe='<$1 $2src=\"',JGe='v-customlayout';Ehb(1682,1,gse);_.tc=function FGc(){tHc(this.b,Ucb,jcb);jHc(this.b,iwe,a6);lHc(this.b,d1,zwe,new GGc);lHc(this.b,a6,zwe,new IGc);lHc(this.b,Ucb,zwe,new KGc);rHc(this.b,a6,Xqe,new bHc(d1));rHc(this.b,a6,Vqe,new bHc(Ucb));CGc(this.b);pHc(this.b,Ucb,FGe,new cHc(Zxe,wC(sC(A_,1),Awe,4,0,[new bHc(Nbb),new bHc(kfb)])));pHc(this.b,Ucb,GGe,new bHc(kfb));pHc(this.b,Ucb,HGe,new bHc(kfb));Fnc((!ync&&(ync=new Lnc),ync),this.a.d)};Ehb(1684,1,Oze,GGc);_.bk=function HGc(a,b){return new OQc};var m_=SYd(Que,'ConnectorBundleLoaderImpl/9/1/1',1684);Ehb(1685,1,Oze,IGc);_.bk=function JGc(a,b){return new xmd};var n_=SYd(Que,'ConnectorBundleLoaderImpl/9/1/2',1685);Ehb(1686,1,Oze,KGc);_.bk=function LGc(a,b){return new WTd};var o_=SYd(Que,'ConnectorBundleLoaderImpl/9/1/3',1686);Ehb(246,202,{14:1,11:1,13:1,12:1,25:1,29:1,15:1,27:1,10:1,9:1,246:1,20:1},OQc);_.bf=function PQc(a){throw new x_d};_.cf=function QQc(){Npb(this);this.f.cf();this.a.cf()};_.Rk=function SQc(){};_.xe=function TQc(a){wpb(this,a);slb();if(mnb((Yk(),a).type)==Jfe){a6b(this,true);lnb(a,true)}};_.Ye=function UQc(){xpb(this);!!this.c&&(this.c.notifyChildrenOfSizeChange=null,undefined)};_.df=function VQc(a){return KQc(this,a)};_.Qe=function WQc(a){Fk((slb(),this._b),a);jpb(this._b,_Ae,true)};_.d=false;_.i='';var d1=SYd(Qhe,'VCustomLayout',246);Ehb(1683,484,{7:1,16:1,128:1,102:1,136:1,26:1,35:1,34:1,30:1,151:1,253:1,31:1,3:1},xmd);_.bi=function ymd(){return !this.L&&(this.L=qSb(this)),DC(DC(DC(this.L,6),147),444)};_.qh=function zmd(){return !this.B&&(this.B=YSb(this)),DC(this.B,246)};_.bh=function Amd(){(!this.B&&(this.B=YSb(this)),DC(this.B,246)).b=this.D;(!this.B&&(this.B=YSb(this)),DC(this.B,246)).g=this.G};_.ti=function Bmd(){GQc((!this.B&&(this.B=YSb(this)),DC(this.B,246),zlb(Fob((!this.B&&(this.B=YSb(this)),DC(this.B,246))))))};_.Mh=function Cmd(b){var c,d,e,f,g,h;wmd(this);for(d=PUb(this).kf();d.Df();){c=DC(d.Ef(),16);e=FC((!this.L&&(this.L=qSb(this)),DC(DC(DC(this.L,6),147),444)).a.no(c));try{MQc((!this.B&&(this.B=YSb(this)),DC(this.B,246)),c.qh(),e)}catch(a){a=Bhb(a);if(HC(a,37)){nbe(pbe((MYd(a6),a6.k)),\"Child not rendered as no slot with id '\"+e+\"' has been defined\")}else throw Ahb(a)}}for(g=b.a.kf();g.Df();){f=DC(g.Ef(),16);if(f.Zg()==this){continue}h=f.qh();h.We()&&KQc((!this.B&&(this.B=YSb(this)),DC(this.B,246)),h)}};_.eh=function Dmd(a){_Sb(this,a);wmd(this);RQc((!this.B&&(this.B=YSb(this)),DC(this.B,246)).i);(!this.B&&(this.B=YSb(this)),DC(this.B,246)).i=null};_.Nh=function Emd(a){NQc((!this.B&&(this.B=YSb(this)),DC(this.B,246)),a)};_.Ih=function Fmd(a,b){};_.a=false;var a6=SYd('com.vaadin.client.ui.customlayout',uAe,1683);Ehb(444,147,{6:1,47:1,147:1,444:1,3:1},WTd);var Ucb=SYd('com.vaadin.shared.ui.customlayout','CustomLayoutState',444);qbe(Uh)(9);\n//# sourceURL=com.example.mobile.widgetset.MobileWidgetset-9.js\n")
