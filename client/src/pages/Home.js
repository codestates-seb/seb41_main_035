import styled from 'styled-components';
import PostBox from '../components/PostBox';
import dummyData from '../db/dummyData.json';
import { useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';
const BREAK_POINT_TABLET = 800;
const BREAK_POINT_PC = 1300;
const Home = () => {
  const location = useLocation();
  const dummy = dummyData.posts;
  const [data, setData] = useState([]);
  console.log(location);
  useEffect(() => {
    window.scrollTo(0, 0);
    if (location.pathname === '/') {
      // postCate();
      setData(dummy);
      console.log(dummy);
    }
  }, [location.pathname]);
  return (
    <>
      <SWrapper>
        <div className="home">
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
    width: 75%;
  }
  .post {
    width: 80%;
    display: flex;
    flex-direction: column;
    justify-content: center;
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
    color: #196ba5;
    cursor: pointer;
    /* font-family: 'Gowun Batang', serif; */
    font-family: 'Song Myung', serif;
    /* font-family: 'Source Sans Pro', sans-serif; */
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        margin: 25px 0px;
        font-size: 1rem;
      }
    }
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        margin: 25px 0px;
        font-size: 1.2rem;
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
