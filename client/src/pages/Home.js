import styled from 'styled-components';
import PostBox from '../components/PostBox';

const Home = () => {
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
            <PostBox />
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
    font-size: 25px;
    width: 130px;
    height: 50px;
    border: none;
    color: #196ba5;
    cursor: pointer;
    /* font-family: 'Gowun Batang', serif; */
    font-family: 'Song Myung', serif;
    /* font-family: 'Source Sans Pro', sans-serif; */
  }
  p {
    margin: 25px;
  }
`;

export default Home;
