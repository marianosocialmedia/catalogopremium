const clamp=(n,min,max)=>Math.min(Math.max(n,min),max);
const progressBar=document.getElementById("progressBar");
const productsRoot=document.getElementById("produtos");

function esc(value=""){return String(value).replace(/[&<>"']/g,m=>({"&":"&amp;","<":"&lt;",">":"&gt;","\\"":"&quot;","'":"&#039;"}[m]));}

function renderProducts(){
  const products=window.PRODUCTS||[];
  productsRoot.innerHTML=products.map((p,i)=>{
    const reverse=i%2===1?"reverse":"";
    return `
      <article class="product" data-product="${i}">
        <div class="product-panel ${reverse}">
          <div class="product-copy">
            <span class="product-index">${String(i+1).padStart(2,"0")} / ${String(products.length).padStart(2,"0")}</span>
            <p class="product-kicker">${esc(p.subtitle)}</p>
            <h3>${esc(p.name)}</h3>
            <p class="product-price">${esc(p.price)}</p>
            <p class="product-note">${esc(p.note)}</p>
            <span class="product-tag">SELEÇÃO MONJAROS</span>
          </div>
          <div class="product-visual">
            <div class="product-glow accent-${esc(p.accent)}"></div>
            <div class="product-img-wrap">
              <img class="product-img" src="${esc(p.image)}" alt="${esc(p.name)}" loading="lazy">
              <div class="image-placeholder">
                <strong>${esc(p.name.split(" ")[0])}</strong>
                <span>${esc(p.image)}</span>
              </div>
            </div>
          </div>
        </div>
      </article>`;
  }).join("");

  document.querySelectorAll(".product-img").forEach(img=>{
    const placeholder=img.nextElementSibling;
    img.addEventListener("load",()=>placeholder.style.display="none");
    img.addEventListener("error",()=>{img.style.display="none";placeholder.style.display="flex"});
  });
}
renderProducts();

function sectionProgress(el){
  const r=el.getBoundingClientRect();
  return clamp(-r.top/Math.max(el.offsetHeight-innerHeight,1),0,1);
}

function update(){
  const doc=document.documentElement;
  progressBar.style.width=`${scrollY/Math.max(doc.scrollHeight-innerHeight,1)*100}%`;

  const device=document.querySelector(".device-frame");
  const hero=document.querySelector(".hero");
  if(device && hero){
    const p=sectionProgress(hero);
    device.style.transform=`translate(-50%,-50%) rotateX(${58-p*17}deg) rotateZ(${-4+p*5}deg) scale(${1+p*.05})`;
  }

  document.querySelectorAll(".product").forEach(section=>{
    const p=sectionProgress(section);
    const wrap=section.querySelector(".product-img-wrap");
    const glow=section.querySelector(".product-glow");
    if(wrap) wrap.style.transform=`rotateY(${-12+p*24}deg) rotateX(${5-p*8}deg) translateY(${(p-.5)*-18}px) scale(${.93+p*.08})`;
    if(glow){
      glow.style.transform=`scale(${.86+p*.2})`;
      glow.style.opacity=String(.25+Math.sin(p*Math.PI)*.6);
    }
  });
}
addEventListener("scroll",update,{passive:true});
addEventListener("resize",update);
update();

const cursor=document.getElementById("cursorGlow");
addEventListener("pointermove",e=>{
  cursor.style.left=e.clientX+"px";
  cursor.style.top=e.clientY+"px";
},{passive:true});

const drawer=document.getElementById("drawer");
const setDrawer=open=>{
  drawer.classList.toggle("open",open);
  drawer.setAttribute("aria-hidden",String(!open));
  document.body.style.overflow=open?"hidden":"";
};
document.getElementById("menuBtn").addEventListener("click",()=>setDrawer(true));
document.getElementById("closeDrawer").addEventListener("click",()=>setDrawer(false));
drawer.querySelectorAll("a").forEach(a=>a.addEventListener("click",()=>setDrawer(false)));
