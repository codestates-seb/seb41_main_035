import styled from 'styled-components';
import Sidebar from '../components/Sidebar';
import PostBox from '../components/PostBox';
const Home = () => {
  return (
    <>
      <SWrapper>
        <Sidebar />
        <div className="main post">
          <Filter>
            <button className="filter button">Hot</button>
            <p>/</p>
            <button className="filter button">New</button>
            <p>/</p>
            <button className="filter button">대여 가능</button>
            <p>/</p>
            <button className="filter button">Follow</button>
          </Filter>
          <PostBox />
        </div>
      </SWrapper>
    </>
  );
};
const SWrapper = styled.div`
  display: flex;
  .post {
    width: 100%;
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
  height: 110px;
  button {
    margin: 25px 5px;
    font-size: 30px;
    width: 130px;
    height: 50px;
    border: none;
    color: #196ba5;
    cursor: pointer;
  }
  p {
    margin: 30px;
  }
`;

export default Home;
