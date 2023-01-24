import styled from 'styled-components';
import memberstore from '../store/memberstore';
import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const Google = () => {
  const { setisLogin } = memberstore((state) => state);
  const navigate = useNavigate();
  const params = useParams();

  useEffect(() => {
    const accessToken = params.access_token;
    const refreshToken = params.refresh_token;

    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    setisLogin(true);

    navigate('/');
  }, []);
  return <></>;
};

export default Google;
