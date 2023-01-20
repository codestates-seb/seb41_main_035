import axios from 'axios';

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
      'Content-Type': 'application/json',
    },
    data,
  }).catch((err) => console.log('Error', err.message));
};

export const fetchDelete = (url) => {
  axios(url, {
    method: 'delete',
  })
    .then((res) => {
      if (res) {
        window.location.replace('/postview/:id'); //새로고침
      }
    })
    .catch((err) => console.log('Error', err.message));
};
