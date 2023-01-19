import styled from 'styled-components';
import Sidebar from '../components/Sidebar';
import PostBox from '../components/PostBox';
import { BiCaretDownCircle } from 'react-icons/bi';
const Shoes = () => {
  const onDpMenu = () => {
    let click = document.getElementById('drop-content');
    if (click.style.display === 'none') {
      click.style.display = 'flex';
    } else {
      click.style.display = 'none';
    }
  };
  return (

    <SWrapper>
      <div className="shoes">

        <div className="main post">
          <span className="category-name">신발</span>
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
      </div>
    </SWrapper>
  );
};
const SWrapper = styled.div`
  display: flex;
  justify-content: flex-end;
  .shoes {
    width: 75%;
  }
  .post {
    width: 80%;
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
  width: 105%;
  justify-content: space-between;
  .availability {
    margin-top: 50px;
    margin-bottom: 10px;
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
export default Shoes;
