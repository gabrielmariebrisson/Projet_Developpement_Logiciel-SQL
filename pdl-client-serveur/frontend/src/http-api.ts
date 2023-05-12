import axios, { AxiosResponse, AxiosError } from 'axios';
import { ImageType } from '@/image';

const instance = axios.create({
  baseURL: "/",
  timeout: 15000,
});

const responseBody = (response: AxiosResponse) => response.data;

const requests = {
  get: (url: string, param: {}) => instance.get(url, param).then(responseBody),
  post: (url: string, body: {}) => instance.post(url, body, { headers: { "Content-Type": "multipart/form-data" }, }).then(responseBody),
  put: (url: string, body: {}) => instance.put(url, body).then(responseBody),
  delete: (url: string) => instance.delete(url).then(responseBody)
};

export const api = {
  getImageList: ( monId: number): Promise<ImageType[]> => requests.get(`images?monId=${monId}`, {}),
  getImageListPublic: ( ): Promise<ImageType[]> => requests.get(`images`, {}),
  getImage: (id: number): Promise<Blob> => requests.get(`images/${id}`, { responseType: "blob" }),
  getAlgorithme : (id: number, algorithm: string): Promise<Blob> => requests.get(`images/${id}?algorithm=${algorithm}`, { responseType: "blob" }),
  getAlgorithme_p1 : (id: number, algorithm: string, p1: number): Promise<Blob> => requests.get(`images/${id}?algorithm=${algorithm}&p1=${p1}`, { responseType: "blob" }),
  getAlgorithme_p1_p2 : (id: number, algorithm: string, p1: number, p2: number): Promise<Blob> => requests.get(`images/${id}?algorithm=${algorithm}&p1=${p1}&p2=${p2}`, { responseType: "blob" }),
  createImage: (form: FormData, monId: number, monStatut: number): Promise<ImageType> => requests.post(`images?monId=${monId}&monStatut=${monStatut}`, form),
  //createImage: (form: FormData, monId: number): Promise<ImageType> => requests.post(`images?monId=${monId}`, form),
  deleteImage: (id: number): Promise<void> => requests.delete(`images/${id}`),

  postUser: (pseudo: string, password: string, mail: string, birthday: Date) => requests.post(`register?pseudo=${pseudo}&password=${password}&mail=${mail}&birthday=${birthday.toISOString().slice(0, 10)}`, {}),
  getAllUsers: (): Promise<any[]> => requests.get('users', {}),

  postLogin: (mail: string, password: string) => requests.post(`login?mail=${mail}&password=${password}`, {}),
  modifyUser: (pseudo: string, password: string, mail: string, birthday: Date, statut: string, monId: number) => requests.post(`modify?pseudo=${pseudo}&password=${password}&mail=${mail}&birthday=${birthday.toISOString().slice(0, 10)}&statut=${statut}&monId=${monId}`, {}),
  deleteUser: (idUser: number) => requests.post(`delete?monId=${idUser}`, {}),

  winCoins: (monId: number, coins: number) => requests.post(`winCoins?monId=${monId}&coins=${coins}`, {}),

  restoreThisUser: (idUser: number) => requests.post(`restore?idUser=${idUser}`, {}),
 
  getUsersCoins:() => requests.post(`getUsersCoins`, {}),

  selectImageWithIdUser:(idUser: number)  => requests.post(`selectImageWithIdUser?idUser=${idUser}`, {}),

  changeImgStatut:(ImgId: number, statutId: number)  => requests.post(`changeImgStatut?ImgId=${ImgId}&statutId=${statutId}`, {}),
  //Like image
  likeImage: (id: number,monId: number) => requests.post(`like?imageId=${id}&userId=${monId}`, {} ),
  unlikeImage: (id: number,monId: number) => requests.delete(`like?imageId=${id}&userId=${monId}`),
  checkIfUserLikedImage: (id: number,monId: number) => requests.get(`like?imageId=${id}&userId=${monId}`, {}),
  getImageLikeCount: (id: number) => requests.get(`/like/count?imageId=${id}`, {}),
  
  
};


