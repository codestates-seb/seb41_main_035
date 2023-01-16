import styled from 'styled-components';
import Sidebar from '../components/Sidebar';
import PostBox from '../components/PostBox';
import { BiCaretDownCircle } from 'react-icons/bi';
const Top = () => {
  const onDpMenu = () => {
    let click = document.getElementById('drop-content');
    if (click.style.display === 'none') {
      click.style.display = 'flex';
    } else {
      click.style.display = 'none';
    }
  };
  return (
    <>
      <SWrapper>
        <Sidebar />
        <div className="main post">
          <span className="category-name">상의</span>
          <Filter>
            <div className="rental availability ">
              <input type="checkbox" name="rental" value="" />
              렌탈 가능
            </div>
            <div className="dropdown">
              <button className="dropdown_button" onClick={onDpMenu}>
                <BiCaretDownCircle />
                정렬 순서
              </button>
              <div id="drop-content">
                <button>가격 낮은순</button>
                <button>가격 높은순</button>
                <button>최신순</button>
              </div>
            </div>
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
  .category-name {
    text-align: center;
    font-size: 50px;
    margin-top: 50px;
  }
`;
const Filter = styled.div`
  display: flex;
  width: 69%;
  justify-content: space-between;
  .availability {
    margin-top: 50px;
    margin-bottom: 20px;
  }
  .dropdown_button {
    margin-top: 40px;
    font-size: 20px;
    width: 120px;
    height: 35px;
    position: releative;
    border: none;
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  #drop-content {
    display: flex;
    display: none;
    width: 120px;
    flex-direction: column;
    position: absolute;
    z-index: 999;
    button {
      height: 30px;
      border: 0.1px solid gray;
      cursor: pointer;
      background-color: #eee5ca;
    }
  }
`;
export default Top;
