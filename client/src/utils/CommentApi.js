import axios from 'axios';
import { token } from '../constants/index';
export const fetchCreate = (url, data) => {
  axios(url, {
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
    },
    data: JSON.stringify({
      data,
    }),
  })
    .then((res) => {
      if (res) {
        window.location.replace('/postview/:id'); //새로고침
      }
    })
    .catch((err) => console.log('Error', err.message));
};

export const fetchPatch = (url, data) => {
  axios(url, {
    method: 'patch',
    headers: {
      Authorization: token,
    },
    data,
  }).catch((err) => console.log('Error', err.message));
};

export const fetchDelete = (url) => {
  axios(url, {
    method: 'delete',
    headers: {
      Authorization: token,
    },
  })
    .then((res) => {
      if (res) {
        window.location.replace('/postview/:id'); //새로고침
      }
    })
    .catch((err) => console.log('Error', err.message));
};
