//import { api } from '@/http-api';
//import router from "@/router";

export interface ImageType {
  id: number;
  name: string;
}
/*
const props = defineProps<{ id: number }>()


export function deleteFile() 
{
  api.deleteImage(props.id).then(() => {
  }).catch(e => {
    console.log(e.message);
  });
  router.push({ name: 'home'});
  //getImageList();
}



export function lecture_data(data: Blob){
  const reader = new window.FileReader();
    reader.readAsDataURL(data);
    reader.onload = () => {
      const galleryElt = document.getElementById("gallery-"+props.id);
      if (galleryElt !== null) {
        const imgElt = document.createElement("img");
        galleryElt.appendChild(imgElt);
        if (imgElt !== null && reader.result as string) {
          imgElt.setAttribute("src", (reader.result as string));
        }
      }
    };
}

export function telechargement_image(data: Blob,name :string) {
  const link = document.createElement("a");
  const reader = new window.FileReader();

  reader.readAsDataURL(data);
  
  reader.onload = () => {
    link.href = reader.result as string;
    link.download = name;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };
}

export function downloadImage() 
{
  const image = document.querySelector(`#gallery-${props.id} img`) as HTMLImageElement;
  const link = document.createElement("a");
  link.href = image.src;
  link.download = "image.jpg";
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

export function modifyWantPicture(want_picture :boolean){
  if(want_picture){
    want_picture=false;
  }else{
    want_picture=true;
  }
  return want_picture;
}
*/