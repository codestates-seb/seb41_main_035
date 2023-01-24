import styled from 'styled-components';
import PostBox from '../components/PostBox';
import { useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
import axios from 'axios';

import Slider from '../components/Slider';
const Home = () => {
  const location = useLocation();
  const [data, setData] = useState([]);
  useEffect(() => {
    window.scrollTo(0, 0);
    const fetchData = async () => {
      try {
        if (location.pathname === '/') {
          const response = await axios.get(`http://13.125.30.88/boards`);
          setData(response.data.data);
        }
      } catch {
        window.alert('오류가 발생했습니다.');
      }
    };
    fetchData();
  }, [location.pathname]);
  return (
    <>
      <SWrapper>
        <div className="home">
          <Slider />
          <div className="main post">
            <Filter>
              <button className="filter button">Hot</button>
              <p>/</p>
              <button className="filter button">New</button>
              <p>/</p>
              <button className="filter button">Rent</button>
              <p>/</p>
              <button className="filter button">Follow</button>
            </Filter>
            <PostBox data={data} />
          </div>
        </div>
      </SWrapper>
    </>
  );
};
const SWrapper = styled.div`
  display: flex;
  justify-content: flex-end;

  .home {
    width: 85%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    left: 0;
    right: 0;
    top: 0;
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      padding: 0 10px;
    }
  }
  .post {
    display: flex;
    /* max-width: 1200px; */
    flex-direction: column;
    justify-content: flex-end;
    align-items: center;
  }
`;
const Filter = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 150px;
  button {
    margin: 25px 5px;
    font-size: 1.5rem;
    width: 130px;
    height: 50px;
    border: none;
    background-color: white;
    cursor: pointer;
    font-family: 'Song Myung', serif;
    &:focus {
      border: 2px solid #1a6aa4;
      border-top: 0;
      border-left: 0;
      border-right: 0;
    }
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        width: 80px;
        margin: 25px 3px;
        font-size: 1.3rem;
      }
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        width: 60px;
        margin: 25px 0px;
        font-size: 1rem;
      }
    }
  }
  p {
    margin: 25px;
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        margin: 0px;
      }
    }
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        margin: 10px;
      }
    }
  }
`;

export default Home;
