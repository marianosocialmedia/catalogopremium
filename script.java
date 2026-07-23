const clamp=(n,min,max)=>Math.min(Math.max(n,min),max);
const ampoule=document.getElementById("ampoule");
const shadow=document.getElementById("heroShadow");
const ring=document.getElementById("impactRing");
const progressBar=document.getElementById("progressBar");

function sectionProgress(el){
  const r=el.getBoundingClientRect();
  const total=el.offsetHeight-innerHeight;
  return clamp(-r.top/Math.max(total,1),0,1);
}

function update(){
  const doc=document.documentElement;
  const pageProgress=scrollY/(doc.scrollHeight-innerHeight);
  progressBar.style.width=`${pageProgress*100}%`;

  const hero=document.querySelector(".hero");
  const hp=sectionProgress(hero);
  const fall=document.querySelector(".fall-zone");
  const fp=sectionProgress(fall);

  const x=hp*6;
  const y=hp*75;
  const rot=9+hp*120;
  const scale=1-hp*.08;
  ampoule.style.transform=`translate(calc(-50% + ${x}vw),calc(-50% + ${y}vh)) rotate(${rot}deg) scale(${scale})`;
  shadow.style.opacity=String(1-hp*.9);
  shadow.style.transform=`translateX(-50%) scale(${1-hp*.55})`;

  if(fp<.48){
    const fallY=fp*88;
    const fallRot=130+fp*280;
    ampoule.style.position="fixed";
    ampoule.style.top="10vh";
    ampoule.style.transform=`translate(calc(-50% + 6vw), ${fallY}vh) rotate(${fallRot}deg) scale(.92)`;
    ampoule.style.opacity="1";
    ampoule.style.zIndex="20";
  }else{
    const fade=clamp((fp-.48)/.12,0,1);
    ampoule.style.opacity=String(1-fade);
    ampoule.style.position="fixed";
    ring.style.opacity=String(1-fade);
    ring.style.transform=`translate(-50%,-50%) scale(${.2+fade*2.8})`;
  }

  if(scrollY < hero.offsetHeight*.8){
    ampoule.style.opacity="1";
  }

  document.querySelectorAll(".product").forEach((section)=>{
    const p=sectionProgress(section);
    const pack=section.querySelector(".fictional-pack");
    const vial=section.querySelector(".fictional-vial");
    const pen=section.querySelector(".fictional-pen");
    const orbital=section.querySelector(".orbital");
    const copy=section.querySelector(".product-copy");

    if(pack){
      pack.style.transform=`rotateY(${-24+p*42}deg) rotateX(${7-p*10}deg) translateY(${(p-.5)*-28}px) scale(${.88+p*.16})`;
    }
    if(vial){
      vial.style.transform=`translate(${(p-.5)*-28}px, ${(p-.5)*34}px) rotate(${(p-.5)*12}deg)`;
    }
    if(pen){
      pen.style.transform=`rotate(${-18+p*24}deg) translateX(${(p-.5)*80}px) scale(${.9+p*.12})`;
    }
    if(orbital){
      orbital.style.transform=`scale(${.8+p*.35}) rotate(${p*45}deg)`;
      orbital.style.opacity=String(.35+Math.sin(p*Math.PI)*.65);
    }
    if(copy){
      copy.style.transform=`translateY(${(p-.5)*-24}px)`;
      copy.style.opacity=String(.45+Math.sin(p*Math.PI)*.55);
    }
  });
}
addEventListener("scroll",update,{passive:true});
addEventListener("resize",update);
update();

const drawer=document.getElementById("catalogDrawer");
const menuBtn=document.getElementById("menuBtn");
const closeDrawer=document.getElementById("closeDrawer");
const setDrawer=(open)=>{
  drawer.classList.toggle("open",open);
  drawer.setAttribute("aria-hidden",String(!open));
  document.body.style.overflow=open?"hidden":"";
};
menuBtn.addEventListener("click",()=>setDrawer(true));
closeDrawer.addEventListener("click",()=>setDrawer(false));
drawer.querySelectorAll("a").forEach(a=>a.addEventListener("click",()=>setDrawer(false)));
